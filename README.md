t2-rest-activity
================

A generic REST service for Taverna Workbench (modified with the Adapter pattern)

List of Modifications
-------------

1.  Added `HTTPRequestHandlerAdapter` class.
2.  Added `RESTActivity#createHTTPRequestHandlerAdapter` method.
3.  Modified `RESTActivity#executeAsync` method so that calls to `HTTPRequestHandler#initiateHTTPRequest` method include result of `RESTActivity#createHTTPRequestHandlerAdapter` method.
4.  Added `HTTPRequestHandler#initiateHTTPRequest` method with `HTTPRequestHandlerAdapter` parameter.
5.  Re-factored old `HTTPRequestHandler#initiateHTTPRequest` method to maintain backwards compatibility.
6.  Modified signatures for `HTTPRequestHandler` private methods to include `HTTPRequestHandlerAdapter` parameter.
7.  Added call to `HTTPRequestHandlerAdapter#modify` to `HTTPRequestHandler#initiateHTTPRequest` method.
8.  Added call to `HTTPRequestHandlerAdapter#finalize` to `HTTPRequestHandler#performHTTPRequest` method.
