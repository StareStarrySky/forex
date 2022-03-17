package xyz.starestarrysky.forex.server.config

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration

@Configuration
class MvcConfig : DelegatingWebMvcConfiguration() {
    public override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val jsonMapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.WRAP_EXCEPTIONS, true)
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            .build()
        converters.forEach {
            if (it is MappingJackson2HttpMessageConverter) {
                it.objectMapper = jsonMapper
            }
        }
        super.extendMessageConverters(converters)
    }
}
