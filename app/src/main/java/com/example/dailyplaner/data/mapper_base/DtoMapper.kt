package com.example.dailyplaner.data.mapper_base

interface DtoMapper<Dto, ViewItem> {
    fun toViewItem(value: Dto): ViewItem
}