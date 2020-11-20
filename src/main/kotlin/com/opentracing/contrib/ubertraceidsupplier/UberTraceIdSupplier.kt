package com.opentracing.contrib.ubertraceidsupplier

import io.opentracing.Tracer
import io.opentracing.propagation.Format
import io.opentracing.propagation.TextMap
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class UberTraceIdSupplier(private val tracer: Tracer) {

    /**
     * If there is an active span on the tracer, inject its context into
     * an UberHeaderInjectAdapter, which can supply the Uber format header
     */
    fun getTraceId(): String? = tracer.activeSpan()?.let {
        UberHeaderInjectAdapter().apply {
            tracer.inject(it.context(), Format.Builtin.HTTP_HEADERS, this)
        }.getTraceId()
    }

    private class UberHeaderInjectAdapter : TextMap {
        private val collector: MutableMap<String?, String?> = hashMapOf()

        override fun put(key: String?, value: String?) {
            collector[key] = value
        }

        override fun iterator(): MutableIterator<MutableMap.MutableEntry<String, String>> {
            throw UnsupportedOperationException("Should be used only with tracer#inject()")
        }

        /**
         * Ensure that the trace ID is not url encoded
         */
        fun getTraceId() = collector["uber-trace-id"]?.let { URLDecoder.decode(it, StandardCharsets.UTF_8) }
    }
}
