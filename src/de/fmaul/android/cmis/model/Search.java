/*
 * Copyright (C) 2010 Jean Marie PASCAL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fmaul.android.cmis.model;

import java.io.Serializable;

public class Search implements Serializable{
	
	private static final long serialVersionUID = 2L;
	
	private long id;
    private String name;
    private String url;
	private long serverId;
	
	public Search(long id, String name, String url, long serverId) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.serverId = serverId;
	}
	
	
	public Search() {
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setServerId(long serverId) {
		this.serverId = serverId;
	}
	public long getServerId() {
		return serverId;
	}
}
