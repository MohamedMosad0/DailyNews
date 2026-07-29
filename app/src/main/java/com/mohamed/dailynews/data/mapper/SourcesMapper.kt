package com.mohamed.dailynews.data.mapper

import com.mohamed.dailynews.data.api.model.SourceDM
import com.mohamed.dailynews.domain.model.Source
import javax.inject.Inject

class SourcesMapper @Inject constructor(){

    fun toSource(sourceDM: SourceDM): Source {
        return Source(
            name = sourceDM.name,
            id = sourceDM.id
        )
    }

    fun toSources(sources: List<SourceDM>): List<Source> {
        return sources.map { sourceDM ->
            return@map Source(
                name = sourceDM.name,
                id = sourceDM.id
            )
        }
    }
}