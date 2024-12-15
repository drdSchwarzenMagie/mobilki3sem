package com.example.money

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amountEditText: EditText = findViewById(R.id.amountEditText)
        val fromCurrencyEditText: EditText = findViewById(R.id.fromCurrencyEditText)
        val toCurrencyEditText: EditText = findViewById(R.id.toCurrencyEditText)
        val convertButton: Button = findViewById(R.id.convertButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        convertButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val fromCurrency = fromCurrencyEditText.text.toString().uppercase()
            val toCurrency = toCurrencyEditText.text.toString().uppercase()

            // Запускаем парсинг и расчет в фоновом потоке
            CoroutineScope(Dispatchers.IO).launch {
                val rates = fetchCurrencyRates()

                val fromRate = rates.find { it.charCode == fromCurrency }?.rate ?: 1.0
                val toRate = rates.find { it.charCode == toCurrency }?.rate ?: 1.0

                val result = if (fromRate != 1.0 && toRate != 1.0) {
                    convertCurrency(amount, fromRate, toRate)
                } else {
                    null
                }

                withContext(Dispatchers.Main) {
                    if (result != null) {
                        resultTextView.text = "Результат: $result $toCurrency"
                    } else {
                        resultTextView.text = "Неверно указана валюта."
                    }
                }
            }
        }
    }

    private fun fetchCurrencyRates(): List<Currency> {
        val url = URL("https://www.cbr.ru/scripts/XML_daily.asp")
        val connection = url.openConnection()
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connection.getInputStream())
        document.documentElement.normalize()

        val currencyList = mutableListOf<Currency>()
        val nodeList = document.getElementsByTagName("Valute")

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i) as Element
            val charCode = node.getElementsByTagName("CharCode").item(0).textContent
            val name = node.getElementsByTagName("Name").item(0).textContent
            val value = node.getElementsByTagName("Value").item(0).textContent.replace(",", ".").toDouble()

            currencyList.add(Currency(charCode, name, value))
        }

        return currencyList
    }

    private fun convertCurrency(amount: Double, fromRate: Double, toRate: Double): Double {
        val rubles = amount * fromRate
        return round((rubles / toRate) * 100) / 100
    }
}

data class Currency(val charCode: String, val name: String, val rate: Double)