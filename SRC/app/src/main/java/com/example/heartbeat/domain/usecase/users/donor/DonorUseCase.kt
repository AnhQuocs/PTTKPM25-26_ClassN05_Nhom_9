package com.example.heartbeat.domain.usecase.users.donor

data class DonorUseCase(
    val addDonorUseCase: AddDonorUseCase,
    val getDonorUseCase: GetDonorUseCase,
    val updateDonorUseCase: UpdateDonorUseCase,
    val isDonorProfileExistUseCase: IsDonorProfileExistUseCase,

    val uploadAvatarUseCase: UploadAvatarUseCase,
    val updateAvatarUseCase: UpdateAvatarUseCase,
    val saveAvatarUrlUseCase: SaveAvatarUrlUseCase,
    val getAvatarUseCase: GetAvatarUseCase
)