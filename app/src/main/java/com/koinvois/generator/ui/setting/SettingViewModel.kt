package com.koinvois.generator.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koinvois.generator.domain.model.PersonalBusiness
import com.koinvois.generator.domain.usecase.business.AddBusinessUseCase
import com.koinvois.generator.domain.usecase.business.DeleteBusinessUseCase
import com.koinvois.generator.domain.usecase.business.GetBusinessUseCase
import com.koinvois.generator.domain.usecase.business.UpdateBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val addBusinessUseCase: AddBusinessUseCase,
    private val updateBusinessUseCase: UpdateBusinessUseCase,
    private val deleteBusinessUseCase: DeleteBusinessUseCase,
    private val getBusinessUseCase: GetBusinessUseCase
) : ViewModel() {

    var businessUpdateModel: MutableLiveData<PersonalBusiness?> = MutableLiveData()

    suspend fun addBusiness(business: PersonalBusiness) {
        addBusinessUseCase(business)
    }

    suspend fun updateBusiness(business: PersonalBusiness) {
        withContext(Dispatchers.IO)
        {
            updateBusinessUseCase(business)
        }
    }

    suspend fun deleteBusiness() {
        businessUpdateModel.value?.let { deleteBusinessUseCase(it) }
    }

    suspend fun getBusiness() {
        businessUpdateModel.postValue(getBusinessUseCase(1))
    }

}