/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This file was generated.
 *  with google-apis-code-generator 1.2.0 (build: 2012-10-03 02:48:15 UTC)
 *  on 2012-10-24 at 06:36:30 UTC 
 */

package com.appspot.api.services.taskendpoint;

import com.google.api.client.googleapis.services.GoogleClient;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.common.base.Preconditions;

import java.io.IOException;

/**
 * Service definition for Taskendpoint (v1).
 *
 * <p>
 * 
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link JsonHttpRequestInitializer} to initialize global parameters via its
 * {@link Builder}. Sample usage:
 * </p>
 *
 * <pre>
  public class TaskendpointRequestInitializer implements JsonHttpRequestInitializer {
      public void initialize(JsonHttpRequest request) {
        TaskendpointRequest taskendpointRequest = (TaskendpointRequest)request;
        taskendpointRequest.setPrettyPrint(true);
        taskendpointRequest.setKey(ClientCredentials.KEY);
    }
  }
 * </pre>
 *
 * @since 1.3.0
 * @author Google, Inc.
 */
public class Taskendpoint extends GoogleClient {

  /**
   * The default encoded base path of the service. This is determined when the library is generated
   * and normally should not be changed.
   * @deprecated (scheduled to be removed in 1.8) Use "/" + {@link #DEFAULT_SERVICE_PATH}.
   */
  @Deprecated
  public static final String DEFAULT_BASE_PATH = "/_ah/api/taskendpoint/v1/";

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myapp.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "taskendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Construct a Taskendpoint instance to connect to the Taskendpoint service.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport The transport to use for requests
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @deprecated (scheduled to be removed in 1.8) Use
   *             {@link #Taskendpoint(HttpTransport, JsonFactory, HttpRequestInitializer)}.
   */
  @Deprecated
  public Taskendpoint(HttpTransport transport, JsonFactory jsonFactory) {
    super(transport, jsonFactory, DEFAULT_BASE_URL);
  }

  /**
   * Construct a Taskendpoint instance to connect to the Taskendpoint service.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport The transport to use for requests
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @param httpRequestInitializer The HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Taskendpoint(HttpTransport transport, JsonFactory jsonFactory,
      HttpRequestInitializer httpRequestInitializer) {
    super(transport, jsonFactory, DEFAULT_ROOT_URL, DEFAULT_SERVICE_PATH, httpRequestInitializer);
  }

  /**
   * Construct a Taskendpoint instance to connect to the Taskendpoint service.
   *
   * @param transport The transport to use for requests
   * @param jsonHttpRequestInitializer The initializer to use when creating an JSON HTTP request
   * @param httpRequestInitializer The initializer to use when creating an {@link HttpRequest}
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @param jsonObjectParser JSON parser to use or {@code null} if unused
   * @param baseUrl The base URL of the service on the server
   * @param applicationName The application name to be sent in the User-Agent header of requests
   */
  @Deprecated
  Taskendpoint(
      HttpTransport transport,
      JsonHttpRequestInitializer jsonHttpRequestInitializer,
      HttpRequestInitializer httpRequestInitializer,
      JsonFactory jsonFactory,
      JsonObjectParser jsonObjectParser,
      String baseUrl,
      String applicationName) {
      super(transport,
          jsonHttpRequestInitializer,
          httpRequestInitializer,
          jsonFactory,
          jsonObjectParser,
          baseUrl,
          applicationName);
  }

  /**
   * Construct a Taskendpoint instance to connect to the Taskendpoint service.
   *
   * @param transport The transport to use for requests
   * @param jsonHttpRequestInitializer The initializer to use when creating an JSON HTTP request
   * @param httpRequestInitializer The initializer to use when creating an {@link HttpRequest}
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @param jsonObjectParser JSON parser to use or {@code null} if unused
   * @param rootUrl The root URL of the service on the server
   * @param servicePath The service path of the service on the server
   * @param applicationName The application name to be sent in the User-Agent header of requests
   * @param suppressPatternChecks whether discovery pattern checks should be suppressed on required
   *        parameters
   */
  Taskendpoint(
      HttpTransport transport,
      JsonHttpRequestInitializer jsonHttpRequestInitializer,
      HttpRequestInitializer httpRequestInitializer,
      JsonFactory jsonFactory,
      JsonObjectParser jsonObjectParser,
      String rootUrl,
      String servicePath,
      String applicationName,
      boolean suppressPatternChecks) {
      super(transport,
          jsonHttpRequestInitializer,
          httpRequestInitializer,
          jsonFactory,
          jsonObjectParser,
          rootUrl,
          servicePath,
          applicationName,
          suppressPatternChecks);
  }

  @Override
  protected void initialize(JsonHttpRequest jsonHttpRequest) throws IOException {
    super.initialize(jsonHttpRequest);
  }

  /**
   * Returns an instance of a new builder.
   *
   * @param transport The transport to use for requests
   * @param jsonFactory A factory for creating JSON parsers and serializers
   * @deprecated (scheduled to removed in 1.8) Use
   *             {@link Builder#Builder(HttpTransport, JsonFactory, HttpRequestInitializer)}.
   */
   @Deprecated
   public static Builder builder(HttpTransport transport, JsonFactory jsonFactory) {
     return new Builder(transport, jsonFactory, new GenericUrl(DEFAULT_BASE_URL));
   }

  /**
   * Create a request for the method "updateTask".
   *
   * This request holds the parameters needed by the the taskendpoint server.  After setting any
   * optional parameters, call the {@link UpdateTask#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.api.services.taskendpoint.model.Task}
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public UpdateTask updateTask(com.appspot.api.services.taskendpoint.model.Task content) throws IOException {
    UpdateTask result = new UpdateTask(content);
    initialize(result);
    return result;
  }

  public class UpdateTask extends TaskendpointRequest {

    private static final String REST_PATH = "task";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    UpdateTask(com.appspot.api.services.taskendpoint.model.Task content) {
      super(Taskendpoint.this, HttpMethod.PUT, REST_PATH, content);
      Preconditions.checkNotNull(content);
    }

    /**
     * Sends the "updateTask" request to the Taskendpoint server.
     *
     * @return the {@link com.appspot.api.services.taskendpoint.model.Task} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.taskendpoint.model.Task execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.taskendpoint.model.Task result = response.parseAs(
          com.appspot.api.services.taskendpoint.model.Task.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "updateTask" request to the Taskendpoint server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Task&gt;() {

         public void onSuccess(Task content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.taskendpoint.model.Task> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.taskendpoint.model.Task.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public UpdateTask setFields(String fields) {
      super.setFields(fields);
      return this;
    }

  }

  /**
   * Create a request for the method "getTask".
   *
   * This request holds the parameters needed by the the taskendpoint server.  After setting any
   * optional parameters, call the {@link GetTask#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public GetTask getTask(String id) throws IOException {
    GetTask result = new GetTask(id);
    initialize(result);
    return result;
  }

  public class GetTask extends TaskendpointRequest {

    private static final String REST_PATH = "task/{id}";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    GetTask(String id) {
      super(Taskendpoint.this, HttpMethod.GET, REST_PATH, null);
      this.id = Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    /**
     * Sends the "getTask" request to the Taskendpoint server.
     *
     * @return the {@link com.appspot.api.services.taskendpoint.model.Task} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.taskendpoint.model.Task execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.taskendpoint.model.Task result = response.parseAs(
          com.appspot.api.services.taskendpoint.model.Task.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "getTask" request to the Taskendpoint server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Task&gt;() {

         public void onSuccess(Task content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.taskendpoint.model.Task> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.taskendpoint.model.Task.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public GetTask setFields(String fields) {
      super.setFields(fields);
      return this;
    }

    @com.google.api.client.util.Key
    private String id;

    /**

     */
    public String getId() {
      return id;
    }

    public GetTask setId(String id) {
      this.id = id;
      return this;
    }

  }

  /**
   * Create a request for the method "removeTask".
   *
   * This request holds the parameters needed by the the taskendpoint server.  After setting any
   * optional parameters, call the {@link RemoveTask#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public RemoveTask removeTask(String id) throws IOException {
    RemoveTask result = new RemoveTask(id);
    initialize(result);
    return result;
  }

  public class RemoveTask extends TaskendpointRequest {

    private static final String REST_PATH = "task/{id}";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    RemoveTask(String id) {
      super(Taskendpoint.this, HttpMethod.DELETE, REST_PATH, null);
      this.id = Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    /**
     * Sends the "removeTask" request to the Taskendpoint server.
     *
     * @return the {@link com.appspot.api.services.taskendpoint.model.Task} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.taskendpoint.model.Task execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.taskendpoint.model.Task result = response.parseAs(
          com.appspot.api.services.taskendpoint.model.Task.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "removeTask" request to the Taskendpoint server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Task&gt;() {

         public void onSuccess(Task content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.taskendpoint.model.Task> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.taskendpoint.model.Task.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public RemoveTask setFields(String fields) {
      super.setFields(fields);
      return this;
    }

    @com.google.api.client.util.Key
    private String id;

    /**

     */
    public String getId() {
      return id;
    }

    public RemoveTask setId(String id) {
      this.id = id;
      return this;
    }

  }

  /**
   * Create a request for the method "insertTask".
   *
   * This request holds the parameters needed by the the taskendpoint server.  After setting any
   * optional parameters, call the {@link InsertTask#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.api.services.taskendpoint.model.Task}
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public InsertTask insertTask(com.appspot.api.services.taskendpoint.model.Task content) throws IOException {
    InsertTask result = new InsertTask(content);
    initialize(result);
    return result;
  }

  public class InsertTask extends TaskendpointRequest {

    private static final String REST_PATH = "task";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    InsertTask(com.appspot.api.services.taskendpoint.model.Task content) {
      super(Taskendpoint.this, HttpMethod.POST, REST_PATH, content);
      Preconditions.checkNotNull(content);
    }

    /**
     * Sends the "insertTask" request to the Taskendpoint server.
     *
     * @return the {@link com.appspot.api.services.taskendpoint.model.Task} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.taskendpoint.model.Task execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.taskendpoint.model.Task result = response.parseAs(
          com.appspot.api.services.taskendpoint.model.Task.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "insertTask" request to the Taskendpoint server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Task&gt;() {

         public void onSuccess(Task content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.taskendpoint.model.Task> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.taskendpoint.model.Task.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public InsertTask setFields(String fields) {
      super.setFields(fields);
      return this;
    }

  }

  /**
   * Create a request for the method "listTask".
   *
   * This request holds the parameters needed by the the taskendpoint server.  After setting any
   * optional parameters, call the {@link ListTask#execute()} method to invoke the remote operation.
   *
   * @return the request
   * @throws IOException if the initialization of the request fails
   */
  public ListTask listTask() throws IOException {
    ListTask result = new ListTask();
    initialize(result);
    return result;
  }

  public class ListTask extends TaskendpointRequest {

    private static final String REST_PATH = "task";

    /**
     * Internal constructor.  Use the convenience method instead.
     */
    ListTask() {
      super(Taskendpoint.this, HttpMethod.GET, REST_PATH, null);
    }

    /**
     * Sends the "listTask" request to the Taskendpoint server.
     *
     * @return the {@link com.appspot.api.services.taskendpoint.model.Tasks} response
     * @throws IOException if the request fails
     */
    public com.appspot.api.services.taskendpoint.model.Tasks execute() throws IOException {
      HttpResponse response = executeUnparsed();
      com.appspot.api.services.taskendpoint.model.Tasks result = response.parseAs(
          com.appspot.api.services.taskendpoint.model.Tasks.class);
      result.setResponseHeaders(response.getHeaders());
      return result;
    }

    /**
     * Queues the "listTask" request to the Taskendpoint server into the given batch request.
     *
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
       request.queue(batchRequest, new JsonBatchCallback&lt;Tasks&gt;() {

         public void onSuccess(Tasks content, GoogleHeaders responseHeaders) {
           log("Success");
         }

         public void onFailure(GoogleJsonError e, GoogleHeaders responseHeaders) {
           log(e.getMessage());
         }
       });
     * </pre>
     *
     * @param batch a single batch of requests
     * @param callback batch callback
     * @since 1.6
     */
    public void queue(com.google.api.client.googleapis.batch.BatchRequest batch,
        com.google.api.client.googleapis.batch.json.JsonBatchCallback<com.appspot.api.services.taskendpoint.model.Tasks> callback)
        throws IOException {
      batch.queue(buildHttpRequest(), com.appspot.api.services.taskendpoint.model.Tasks.class,
          com.google.api.client.googleapis.json.GoogleJsonErrorContainer.class, callback);
    }

    /**
     * @since 1.7
     */
    @Override
    public ListTask setFields(String fields) {
      super.setFields(fields);
      return this;
    }

  }

  /**
   * Builder for {@link Taskendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends GoogleClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport The transport to use for requests
     * @param jsonFactory A factory for creating JSON parsers and serializers
     * @param baseUrl The base URL of the service. Must end with a "/"
     */
    @Deprecated
    Builder(HttpTransport transport, JsonFactory jsonFactory, GenericUrl baseUrl) {
      super(transport, jsonFactory, baseUrl);
    }

    /**
     * Returns an instance of a new builder.
     *
     * @param transport The transport to use for requests
     * @param jsonFactory A factory for creating JSON parsers and serializers
     * @param httpRequestInitializer The HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(HttpTransport transport, JsonFactory jsonFactory,
        HttpRequestInitializer httpRequestInitializer) {
      super(transport, jsonFactory, DEFAULT_ROOT_URL, DEFAULT_SERVICE_PATH, httpRequestInitializer);
    }

    /** Builds a new instance of {@link Taskendpoint}. */
    @SuppressWarnings("deprecation")
    @Override
    public Taskendpoint build() {
      if (isBaseUrlUsed()) {
        return new Taskendpoint(
            getTransport(),
            getJsonHttpRequestInitializer(),
            getHttpRequestInitializer(),
            getJsonFactory(),
            getObjectParser(),
            getBaseUrl().build(),
            getApplicationName());
      }
      return new Taskendpoint(
          getTransport(),
          getJsonHttpRequestInitializer(),
          getHttpRequestInitializer(),
          getJsonFactory(),
          getObjectParser(),
          getRootUrl(),
          getServicePath(),
          getApplicationName(),
          getSuppressPatternChecks());
    }

    @Override
    @Deprecated
    public Builder setBaseUrl(GenericUrl baseUrl) {
      super.setBaseUrl(baseUrl);
      return this;
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      super.setRootUrl(rootUrl);
      return this;
    }

    @Override
    public Builder setServicePath(String servicePath) {
      super.setServicePath(servicePath);
      return this;
    }

    @Override
    public Builder setJsonHttpRequestInitializer(
        JsonHttpRequestInitializer jsonHttpRequestInitializer) {
      super.setJsonHttpRequestInitializer(jsonHttpRequestInitializer);
      return this;
    }

    @Override
    public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
      super.setHttpRequestInitializer(httpRequestInitializer);
      return this;
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      super.setApplicationName(applicationName);
      return this;
    }

    @Override
    public Builder setObjectParser(JsonObjectParser parser) {
      super.setObjectParser(parser);
      return this;
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      super.setSuppressPatternChecks(suppressPatternChecks);
      return this;
    }
  }
}
