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
 * Warning! This file is generated. Modify at your own risk.
 */

package com.appspot.api.services.taskendpoint.model;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;

/**
 * Model definition for Task.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the . For a detailed explanation see:
 * <a href="http://code.google.com/p/google-api-java-client/wiki/Json">http://code.google.com/p/google-api-java-client/wiki/Json</a>
 * </p>
 *
 * @author Google, Inc.
 */
public final class Task extends GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long priority;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Boolean checked;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long userId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String note;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String encodedKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @JsonString
  private Long dueTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Boolean noDueTime;

  /**

   * The value returned may be {@code null}.
   */
  public Long getPriority() {
    return priority;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setPriority(Long priority) {
    this.priority = priority;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Boolean getChecked() {
    return checked;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setChecked(Boolean checked) {
    this.checked = checked;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getName() {
    return name;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setName(String name) {
    this.name = name;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Long getUserId() {
    return userId;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getNote() {
    return note;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setNote(String note) {
    this.note = note;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public String getEncodedKey() {
    return encodedKey;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setEncodedKey(String encodedKey) {
    this.encodedKey = encodedKey;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Long getDueTime() {
    return dueTime;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setDueTime(Long dueTime) {
    this.dueTime = dueTime;
    return this;
  }

  /**

   * The value returned may be {@code null}.
   */
  public Boolean getNoDueTime() {
    return noDueTime;
  }

  /**

   * The value set may be {@code null}.
   */
  public Task setNoDueTime(Boolean noDueTime) {
    this.noDueTime = noDueTime;
    return this;
  }

  private HttpHeaders responseHeaders;

  /**
   * Sets the HTTP headers returned with the server response, or <code>null</code>.
   *
   * This member should only be non-null if this object was the top level element of a response. For
   * example, a request that returns a single {@link Task} would include the response headers, while
   * a request which returns an array of {@link Task}, would have a non-null response header in the
   * enclosing object only.
   */
  public void setResponseHeaders(HttpHeaders responseHeaders) {
    this.responseHeaders = responseHeaders;
  }

  /**
   * Returns the HTTP headers that were returned with the server response, or
   * <code>null</code>.
   */
  public HttpHeaders getResponseHeaders() {
    return responseHeaders;
  }

}