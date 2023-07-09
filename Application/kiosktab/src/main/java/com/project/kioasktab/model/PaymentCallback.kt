package com.project.kioasktab.model

import com.tosspayments.paymentsdk.model.TossPaymentResult

interface PaymentCallback {
    fun onPaymentSuccess(success: TossPaymentResult.Success)
    fun onPaymentFailed(failed: TossPaymentResult.Fail)
}