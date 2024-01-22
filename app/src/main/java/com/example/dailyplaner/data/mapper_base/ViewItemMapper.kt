package com.example.dailyplaner.data.mapper_base

interface ViewItemMapper<ViewItem, Dto> {
    fun toDto(item: ViewItem): Dto
}