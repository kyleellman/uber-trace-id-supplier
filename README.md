# UberTraceIdSupplier

A simple utility to fetch the [Uber trace ID](https://www.jaegertracing.io/docs/1.7/client-libraries/#key).

Typically, the Uber trace ID is used as a request header,
but it can be useful to access it on its own in order to add to log entries you want to correlate with a distributed trace or a specific span within that trace.

This library simplifies accessing the Uber trace ID.

## Usage

```kt

// Instantiate the supplier
val uberTraceIdSupplier = UberTraceIdSupplier(GlobalTracer.get())

// access the trace ID
val uberFormatTraceId = uberTraceIdSupplier.getTraceId()
```
