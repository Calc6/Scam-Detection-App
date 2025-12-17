package org.setu.scamdetector.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanResultModel(var id: Long = 0,
                           var title: String? = "",
                           var score: Int = 0,
                           var isScam: Boolean = false,
                           var description: String? = ""
) : Parcelable