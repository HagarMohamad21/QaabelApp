package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class NearPlace {

    @SerializedName("geometry")
    @Expose
    private var geometry: Geometry? = null

    @SerializedName("icon")
    @Expose
    private var icon: String? = null
    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("place_id")
    @Expose
    private var placeId: String? = null

    @SerializedName("rating")
    @Expose
    private var rating: Double? = null
    @SerializedName("reference")
    @Expose
    private var reference: String? = null
    @SerializedName("scope")
    @Expose
    private var scope: String? = null
    @SerializedName("types")
    @Expose
    private var types: List<String?>? = null
    @SerializedName("user_ratings_total")
    @Expose
    private var userRatingsTotal: Int? = null
    @SerializedName("vicinity")
    @Expose
    private var vicinity: String? = null
    @SerializedName("numberOfUsers")
    @Expose
    private var numberOfUsers: Int? = null
    @SerializedName("photos")
    @Expose
    private var photos: List<PlacePhoto?>? = null

    fun getGeometry(): Geometry? {
        return geometry
    }

    fun setGeometry(geometry: Geometry?) {
        this.geometry = geometry
    }

    fun getIcon(): String? {
        return icon
    }

    fun setIcon(icon: String?) {
        this.icon = icon
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getPlaceId(): String? {
        return placeId
    }

    fun setPlaceId(placeId: String?) {
        this.placeId = placeId
    }


    fun getRating(): Double? {
        return rating
    }

    fun setRating(rating: Double?) {
        this.rating = rating
    }

    fun getReference(): String? {
        return reference
    }

    fun setReference(reference: String?) {
        this.reference = reference
    }

    fun getScope(): String? {
        return scope
    }

    fun setScope(scope: String?) {
        this.scope = scope
    }

    fun getTypes(): List<String?>? {
        return types
    }

    fun setTypes(types: List<String?>?) {
        this.types = types
    }

    fun getUserRatingsTotal(): Int? {
        return userRatingsTotal
    }

    fun setUserRatingsTotal(userRatingsTotal: Int?) {
        this.userRatingsTotal = userRatingsTotal
    }

    fun getVicinity(): String? {
        return vicinity
    }

    fun setVicinity(vicinity: String?) {
        this.vicinity = vicinity
    }

    fun getNumberOfUsers(): Int? {
        return numberOfUsers
    }

    fun setNumberOfUsers(numberOfUsers: Int?) {
        this.numberOfUsers = numberOfUsers
    }


    fun getPhotos(): List<PlacePhoto?>? {
        return photos
    }

    fun setPhotos(photos: List<PlacePhoto?>?) {
        this.photos = photos
    }
}