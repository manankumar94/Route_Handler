package FactoryPattern;
import FactoryPattern.Requests.HttpRequest;
import FactoryPattern.Responses.HttpResponse;

public interface HttpRequestHandler {
    HttpResponse handleRequest(HttpRequest request) throws Exception;
}

// HttpResponse -> is the return type
// httpRequest -> is the function name
// HttpRequest -> is the input given type 
