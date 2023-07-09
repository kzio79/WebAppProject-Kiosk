package com.project.kioasktab

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.project.kioasktab.databinding.ActivityPayBinding
import com.project.kioasktab.databinding.CustomDialogBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Hashtable

class PayActivity : AppCompatActivity() {
    lateinit var bindingpay: ActivityPayBinding
    lateinit var customDialogBinding: CustomDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingpay = ActivityPayBinding.inflate(layoutInflater)
        setContentView(bindingpay.root)

        val orderid  = intent.getStringExtra("orderId")!!
        val price = intent.getIntExtra("totalPrice",0)
        val customerKey : String = intent.getStringExtra("customerKey")!!
        val ordername: String? = intent.getStringExtra("orderName")

        Log.w("zio","cart에서 pay로 가져오는 값 : $orderid, $ordername")

        val qrCodeWriter = QRCodeWriter()
        val paymentData = JSONObject()
        paymentData.put("orderId",orderid)
        paymentData.put("price",price)
        paymentData.put("orderName",ordername)
        paymentData.put("customerKey", customerKey)

        try {
            val hints = Hashtable<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = Charsets.UTF_8.toString()

            val bitMatrix = qrCodeWriter.encode(paymentData.toString(), BarcodeFormat.QR_CODE, 500, 500)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x,y)) Color.BLACK else Color.WHITE)
                }
            }
            bindingpay.payQrcode.setImageBitmap(bitmap)

            bindingpay.payChange.setOnClickListener {
                bindingpay.payQrcode.visibility = View.GONE
                bindingpay.payVan.visibility = View.VISIBLE
                bindingpay.payResult.visibility = View.VISIBLE
                bindingpay.payChange.visibility = View.GONE
                bindingpay.payQrtext.visibility = View.GONE
                bindingpay.payVantext.visibility = View.VISIBLE
                bindingpay.payCountDown.visibility = View.VISIBLE
            }

            bindingpay.payResult.setOnClickListener {

                val intent = Intent(this, ReceiptActivity::class.java)
                startActivity(intent)
            }

            Handler().postDelayed({

                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.tosspayments.com/v1/payments/orders/$orderid")
                    .get()
                    .addHeader(
                        "Authorization",
                        "Basic dGVzdF9za19aT1J6ZE1hcU4zd015a1p5bDdOcjVBa1lYUUd3Og=="
                    )
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        customDialogBinding = CustomDialogBinding.inflate(layoutInflater)

                        runOnUiThread {
                            if (response.isSuccessful) {
                                Log.w("zio", "결제인증 성공")
                                dialog("결제가 완료 되었습니다.", "결제요청 결과",
                                    customDialogBinding.dialogButton.setOnClickListener {
                                        val successIntent =
                                            Intent(this@PayActivity, ReceiptActivity::class.java)
                                        startActivity(successIntent)
                                        Log.w("zio", "intent: $successIntent")
                                    })
                            } else {
                                Handler().postDelayed({
                                Log.w("zio","결제인증 재 시작: $response, orderid: $orderid")
                                dialog("Qr코드를 다시 스캔해 주세요.", "결제요청 결과",
                                    customDialogBinding.dialogButton.setOnClickListener {
                                        finish()
                                    })
                                },30000)
                            }
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Handler().postDelayed({
                        Log.w("zio", "결제 실패")
                        dialog("결제를 실패했습니다.", "결제요청 결과",
                            customDialogBinding.dialogButton.setOnClickListener {
                                finish()
                            })
                        },30000)
                    }
                })
            }, 20000)//10초 대기

        }catch (e:WriterException){
            e.printStackTrace()
        }

        object : CountDownTimer(60000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                val sec = (millisUntilFinished/1000).toInt()
                bindingpay.payCountDown.text = "$sec 초"

            }

            override fun onFinish() {
                bindingpay.payCountDown.text = "종료"
            }
        }.start()
    }

    //dialog
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

