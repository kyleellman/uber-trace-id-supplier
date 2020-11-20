package com.opentracing.contrib.ubertraceidsupplier

import io.jaegertracing.Configuration
import io.opentracing.noop.NoopTracerFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UberTraceIdSupplierTest {

    @Test
    fun `Using noop tracer does not throw and exception and returns null`() {
        assertNull(UberTraceIdSupplier(NoopTracerFactory.create()).getTraceId())
    }

    @Test
    fun `Returns null for Jaeger tracer when there is no active span`() {
        val tracer = Configuration("test").tracer
        val supplier = UberTraceIdSupplier(tracer)

        assertNull(supplier.getTraceId())
    }

    @Test
    fun `Returns the Uber format tracing header value from Jaeger tracer`() {
        val tracer = Configuration("test").tracer
        val supplier = UberTraceIdSupplier(tracer)

        val span = tracer.buildSpan("test").start()
        tracer.activateSpan(span)

        assertNotNull(supplier.getTraceId())
        span.context().run {
            assertEquals("${toTraceId()}:${toSpanId()}:$parentId:${if (isSampled) 1 else 0}", supplier.getTraceId())
        }
    }
}
