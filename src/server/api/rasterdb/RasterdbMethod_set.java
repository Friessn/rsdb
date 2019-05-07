package server.api.rasterdb;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import broker.Broker;
import broker.Informal.Builder;
import broker.acl.ACL;
import broker.acl.EmptyACL;
import rasterdb.Band;
import rasterdb.RasterDB;
import util.JsonUtil;
import util.Web;

public class RasterdbMethod_set extends RasterdbMethod {
	private static final Logger log = LogManager.getLogger();

	public RasterdbMethod_set(Broker broker) {
		super(broker, "set");	
	}

	@Override
	public void handle(RasterDB rasterdb, String target, Request request, Response response, UserIdentity userIdentity) throws IOException {
		log.info(request);
		request.setHandled(true);
		boolean updateCatalog = false;
		boolean updateCatalogPoints = false;
		try {
			JSONObject json = new JSONObject(Web.requestContentToString(request));
			JSONObject meta = json.getJSONObject("meta");
			Iterator<String> it = meta.keys();
			while(it.hasNext()) {
				String key = it.next();
				switch(key) {
				case "title": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					String title = meta.getString("title").trim();
					log.info("set title " + title);
					Builder informal = rasterdb.informal().toBuilder();
					informal.title = title;
					rasterdb.setInformal(informal.build());
					break;
				}
				case "description": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					String description = meta.getString("description").trim();
					log.info("set description " + description);
					Builder informal = rasterdb.informal().toBuilder();
					informal.description = description;
					rasterdb.setInformal(informal.build());
					break;
				}
				case "corresponding_contact": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					String corresponding_contact = meta.getString("corresponding_contact").trim();
					log.info("set corresponding_contact " + corresponding_contact);
					Builder informal = rasterdb.informal().toBuilder();
					informal.corresponding_contact = corresponding_contact;
					rasterdb.setInformal(informal.build());
					break;
				}
				case "acquisition_date": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					String acquisition_date = meta.getString("acquisition_date").trim();
					log.info("set acquisition_date " + acquisition_date);
					Builder informal = rasterdb.informal().toBuilder();
					informal.acquisition_date = acquisition_date;
					rasterdb.setInformal(informal.build());
					break;
				}
				case "proj4": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					updateCatalogPoints = true;
					String proj4 = meta.getString("proj4").trim();
					log.info("set proj4 "+proj4);
					rasterdb.setProj4(proj4);
					break;
				}
				case "code": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					String code = meta.getString("code").trim();
					log.info("set code "+code);
					rasterdb.setCode(code);
					break;
				}
				case "acl": {
					EmptyACL.ADMIN.check(userIdentity);	
					updateCatalog = true;
					ACL acl = ACL.of(JsonUtil.optStringTrimmedList(meta, "acl"));
					rasterdb.setACL(acl);
					break;
				}
				case "acl_mod": {
					EmptyACL.ADMIN.check(userIdentity);	
					updateCatalog = true;
					ACL acl_mod = ACL.of(JsonUtil.optStringTrimmedList(meta, "acl_mod"));
					rasterdb.setACL_mod(acl_mod);
					break;
				}
				case "tags": {
					rasterdb.checkMod(userIdentity);
					updateCatalog = true;
					Builder informal = rasterdb.informal().toBuilder();
					informal.setTags(JsonUtil.optStringTrimmedArray(meta, "tags"));
					rasterdb.setInformal(informal.build());					
					break;
				}
				case "bands": {
					rasterdb.checkMod(userIdentity);
					JSONArray jsonBands = meta.getJSONArray("bands");
					int bandLen = jsonBands.length();
					for (int i = 0; i < bandLen; i++) {
						JSONObject jsonBand = jsonBands.getJSONObject(i);
						int index = jsonBand.getInt("index");
						Band band = rasterdb.getBandByNumber(index);
						if(band == null) {
							throw new RuntimeException("band does not exist " + index);
						}
						rasterdb.Band.Builder builder = new Band.Builder(band);
						Iterator<String> bandIt = jsonBand.keys();
						while(bandIt.hasNext()) {
							String bandKey = bandIt.next();
							switch(bandKey) {
							case "title": {
								builder.title  = jsonBand.getString("title").trim();
								break;
							}
							case "vis_min": {
								builder.vis_min = JsonUtil.getDouble(jsonBand, "vis_min");
								break;
							}
							case "vis_max": {
								builder.vis_max = JsonUtil.getDouble(jsonBand, "vis_max");
								break;
							}
							default: {					
								log.warn("unknown band key: "+bandKey);
								//throw new RuntimeException("unknown key: "+key);
							}
							}
						}
						rasterdb.setBand(builder.build());
					}
					log.info(jsonBands);
					break;
				}
				default: throw new RuntimeException("unknown key: "+key);
				}
			}
			response.setContentType(MIME_JSON);
			JSONWriter jsonResponse = new JSONWriter(response.getWriter());
			jsonResponse.object();
			jsonResponse.key("response");
			jsonResponse.object();
			jsonResponse.endObject();
			jsonResponse.endObject();
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e);			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			response.setContentType(MIME_JSON);
			JSONWriter json = new JSONWriter(response.getWriter());
			json.object();
			json.key("response");
			json.object();
			json.endObject();
			json.endObject();
		} finally {
			if(updateCatalog) {
				broker.catalog.update(rasterdb, updateCatalogPoints);
			}
		}		
	}
}