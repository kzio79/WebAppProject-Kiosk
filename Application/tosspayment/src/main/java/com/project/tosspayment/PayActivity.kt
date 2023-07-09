package com.project.tosspayment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.tosspayment.databinding.ActivityMainBinding
import com.project.tosspayment.databinding.CustomDialogBinding
import com.project.tosspayment.model.PaymentCallback
import com.tosspayments.paymentsdk.PaymentWidget
import com.tosspayments.paymentsdk.model.TossPaymentResult
import com.tosspayments.paymentsdk.view.Agreement
import com.tosspayments.paymentsdk.view.PaymentMethod
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class PayActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var customDialogBinding: CustomDialogBinding
    private lateinit var paymentWidget: PaymentWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderid  = intent.getStringExtra("orderId")
        val price = intent.getIntExtra("price",0)
        val ordername : String? = intent.getStringExtra("orderName")
        val customerKey: String? = intent.getStringExtra("customerKey")

        //위젯 생성하기 위해 생성
        val methodWidget = binding.paymentWidget
        val agreement = binding.agreementWidget

        paymentWidget = PaymentWidget(this,getString(R.string.payment_ClientKey), customerKey.toString())

        //가격과 주문번호
        paymentWidget.renderPaymentMethods(
            method = methodWidget,
            amount = price
        )

        //이용약관 위젯
        paymentWidget.renderAgreement(agreement)
        //결제위젯 view
        PaymentMethod(this, null)
        //이용약관 view
        Agreement(this, null)

        binding.payAccess.setOnClickListener {
            if (orderid != null && price != null && ordername != null) {
                customDialogBinding =
                    CustomDialogBinding.inflate(LayoutInflater.from(this@PayActivity))

                paymentWidget.requestPayment(
                    paymentInfo = PaymentMethod.PaymentInfo(
                        orderId = orderid.toString(),
                        orderName = ordername.toString()

                    ),
                    paymentCallback = object : PaymentCallback,
                        com.tosspayments.paymentsdk.model.PaymentCallback {

                        override fun onPaymentSuccess(success: TossPaymentResult.Success) {
                            val client = OkHttpClient()

                            val mediaType = MediaType.parse("application/json")
                            val qrBody = RequestBody.create(
                                mediaType,
                                "{\"paymentKey\":\"${success.paymentKey}\",\"amount\":$price,\"orderId\":\"$orderid\",\"customerName\":\"$customerKey\",\"orderName\":\"$ordername\"}"
                            )

                            val request = Request.Builder()
                                .url("https://api.tosspayments.com/v1/payments/confirm")
                                .header(
                                    "Authorization",
                                    "Basic dGVzdF9za19aT1J6ZE1hcU4zd015a1p5bDdOcjVBa1lYUUd3Og=="
                                )
                                .header("Content-Type", "application/json")
                                .post(qrBody)
                                .build()

                            client.newCall(request).enqueue(object : Callback {

                                override fun onResponse(call: Call, response: Response) {
                                    runOnUiThread {
                                        if (response.isSuccessful) {
                                            Log.w("zio", "결제인증 성공")
                                            dialog("결제가 완료 되었습니다.","결제요청 결과",
                                                customDialogBinding.dialogButton.setOnClickListener {
                                                   finishAffinity()
                                                })
                                        } else {
                                            Log.w(
                                                "zio",
                                                "결제인증 재 시작: $response, orderId: $orderid"
                                            )
                                        }
                                    }
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    runOnUiThread {
                                        Log.w("zio", "결제인증 실패")
                                        dialog(
                                            "결제를 실패하였습니다.",
                                            "결제요청 결과",
                                            customDialogBinding.dialogButton.setOnClickListener {
                                                val failIntent =
                                                    Intent(
                                                        this@PayActivity,
                                                        MainActivity::class.java
                                                    )
                                                startActivity(failIntent)
                                            })
                                    }
                                }
                            })
                        }

                        override fun onPaymentFailed(fail: TossPaymentResult.Fail) {
                            Log.d("zio", "결제 실패 ! : $fail")
                            dialog(
                                "결제를 실패 하였습니다.",
                                "결제요청 결과",
                                customDialogBinding.dialogButton.setOnClickListener {
                                    val failIntent =
                                        Intent(this@PayActivity, MainActivity::class.java)
                                    startActivity(failIntent)
                                })
                        }
                    }
                )

            }else{
                Toast.makeText(this, "주문을 확인해 주세요", Toast.LENGTH_LONG).show()
            }

        }

    }
    fun dialog(message:String, title:String, OnClickListener: Unit) {

        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogBinding.customDialog)
            .create()

        customDialogBinding.dialogTitle.text = title
        customDialogBinding.dialogMessage.text = message
        customDialogBinding.dialogButton.text = "확인"
        customDialogBinding.dialogMessage.visibility = View.VISIBLE
        customDialogBinding.dialogButton.visibility = View.VISIBLE

        alertDialog.show()
    }
}
