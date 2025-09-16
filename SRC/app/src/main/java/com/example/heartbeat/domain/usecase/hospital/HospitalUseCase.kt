package com.example.heartbeat.domain.usecase.hospital

class HospitalUseCase (
    val getAllHospitalsUseCase: GetAllHospitalsUseCase,
    val getHospitalByIdUseCase: GetHospitalByIdUseCase
)