/**
    Copyright (C) 2015  FoxGenesis

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.foxgenesis.serverstats.websites;

import static net.foxgenesis.serverstats.Logger.error;

import java.io.IOException;
import java.net.URL;

import net.foxgenesis.helper.SiteReader;

import org.json.JSONObject;

public class CustomJSON extends WebsiteStats {
	public CustomJSON() {
		super("json", '~');
	}

	@Override
	protected String[][] registerTags() {
		return new String[][] {};
	}

	@Override
	protected JSONObject update(URL url) {
		try {
			JSONObject j = SiteReader.readJSON(url);
			useNewKeys(j);
			return j;
		} catch (final IOException e) {
			error(e.getMessage());
		}
		return null;
	}
}
