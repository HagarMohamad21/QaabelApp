package com.Qaabel.org.interfaces

import com.Qaabel.org.model.Api.Response.PlaceResult


interface OnPlaceSelected {
    fun onSelected(place:PlaceResult);
}