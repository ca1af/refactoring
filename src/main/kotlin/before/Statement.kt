package before

import data.Invoice
import data.Play
import java.text.NumberFormat
import java.util.*
import kotlin.math.floor


fun statement(invoice: Invoice, plays: Map<String?, Play?>): String {
    var totalAmount = 0
    var volumeCredits = 0
    val result = StringBuilder(("청구 내역 (고객명: ${invoice.customer})\n"))
    val format = NumberFormat.getCurrencyInstance(Locale.US)

    for (perf in invoice.performances) {
        val play = plays[perf.playID]
        var thisAmount: Int

        when (play!!.type) {
            "tragedy" -> {
                thisAmount = 40000
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30)
                }
            }

            "comedy" -> {
                thisAmount = 30000
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }

            else -> throw IllegalArgumentException("알 수 없는 장르: " + play.type)
        }
        // 포인트를 적립한다.
        volumeCredits += Math.max(perf.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy" == play.type) {
            volumeCredits += (floor((perf.audience / 5).toDouble())).toInt()
        }

        // 청구 내역을 출력한다.
        result.append(
            java.lang.String.format(
                "  %s: %s원 (%d석)\n",
                play.name,
                format.format(thisAmount / 100.0),
                perf.audience
            )
        )
        totalAmount += thisAmount
    }

    result.append(String.format("총액: %s원\n", format.format(totalAmount / 100.0)))
    result.append(String.format("적립 포인트: %d점\n", volumeCredits))

    return result.toString()
}
