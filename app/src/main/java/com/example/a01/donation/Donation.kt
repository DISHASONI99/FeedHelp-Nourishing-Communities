package com.example.a01.donation

data class Donation(
    var userId: String ?= null,
    var cardboardAmount: Long ?= null,
    var metalAmount: Long ?= null,
    var plasticAmount: Long ?= null,
    var address: String ?= null,
)
