package com.example.myapplication.model



data class TracksResponse(
    val query: String,
    val tracks: Tracks
)
data class Tracks(
    val items: List<Item>,
    val totalCount: Int
)
data class Item(
    val `data`: Data
)
data class Data(
    val albumOfTrack: AlbumOfTrack,
    val artists: Artists,
    val contentRating: ContentRating,
    val duration: Duration,
    val id: String,
    val name: String,
    val playability: Playability,
    val uri: String
)
data class AlbumOfTrack(
    val coverArt: CoverArt,
    val id: String,
    val name: String,
    val sharingInfo: SharingInfo,
    val uri: String
)
data class Artists(
    val items: List<ItemX>
)
data class ContentRating(
    val label: String
)
data class Duration(
    val totalMilliseconds: Int
)
data class Playability(
    val playable: Boolean
)
data class SharingInfo(
    val shareUrl: String
)
data class CoverArt(
    val sources: List<Source>
)
data class Source(
    val height: Int,
    val url: String,
    val width: Int
)
data class ItemX(
    val profile: Profile,
    val uri: String
)
data class Profile(
    val name: String
)
