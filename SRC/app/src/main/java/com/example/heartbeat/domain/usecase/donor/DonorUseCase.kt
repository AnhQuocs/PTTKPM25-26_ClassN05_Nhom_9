package com.example.heartbeat.domain.usecase.donor

data class DonorUseCase(
    val addDonorUseCase: AddDonorUseCase,
    val getDonorUseCase: GetDonorUseCase,
    val updateDonorUseCase: UpdateDonorUseCase
)