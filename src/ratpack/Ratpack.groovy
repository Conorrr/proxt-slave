import ratpack.groovy.template.MarkupTemplateModule
import ratpack.http.client.HttpClient
import ratpack.http.client.RequestSpec
import ratpack.http.client.StreamedResponse

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module MarkupTemplateModule
    }

    handlers {
        get "init/:host", { HttpClient httpClient ->
            URI proxyUri = new URI("http://usher.ttvnw.net/api/channel/hls/${pathTokens.host}.m3u8?${request.query}")
            httpClient.requestStream(proxyUri) { RequestSpec spec ->
                spec.headers.copy(request.headers)
                spec.headers.set("Host", "usher.ttvnw.net")
            }.then { StreamedResponse responseStream ->
                responseStream.forwardTo(response)
            }
        }

        files { dir "public" }
    }
}
