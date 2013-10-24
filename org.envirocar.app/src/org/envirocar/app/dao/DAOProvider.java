/* 
 * enviroCar 2013
 * Copyright (C) 2013  
 * Martin Dueren, Jakob Moellers, Gerald Pape, Christopher Stephan
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 * 
 */
package org.envirocar.app.dao;

import org.envirocar.app.dao.cache.CacheSensorDAO;
import org.envirocar.app.dao.cache.CacheTrackDAO;
import org.envirocar.app.dao.cache.CacheUserDAO;
import org.envirocar.app.dao.remote.RemoteSensorDAO;
import org.envirocar.app.dao.remote.RemoteTrackDAO;
import org.envirocar.app.dao.remote.RemoteUserDAO;

/**
 * the {@link DAOProvider} consists a set of methods
 * to access specific DAOs. It checks the internet connection
 * and decides whether it should use a Cache DAO or a Remote one.
 * 
 * @author matthes rieke
 *
 */
public class DAOProvider {
	
	private static DAOProvider _instance;
	private InternetAccessProvider internetAccessProvider;
	private CacheDirectoryProvider cacheDirectoryProvider;

	private DAOProvider(InternetAccessProvider iap, CacheDirectoryProvider cacheDir) {
		this.internetAccessProvider = iap;
		this.cacheDirectoryProvider = cacheDir;
	}


	/**
	 * this create the singleton instance of {@link DAOProvider}.
	 * 
	 * @param iap
	 * @param cacheDir
	 */
	public static void init(InternetAccessProvider iap, CacheDirectoryProvider cacheDir) {
		_instance = new DAOProvider(iap, cacheDir);
	}
	
	public static DAOProvider instance() {
		return _instance;
	}
	
	
	/**
	 * @return the {@link SensorDAO}
	 */
	public SensorDAO getSensorDAO() {
		if (this.internetAccessProvider.isConnected()) {
			return new RemoteSensorDAO(new CacheSensorDAO(this.cacheDirectoryProvider));
		}
		return new CacheSensorDAO(this.cacheDirectoryProvider);
	}
	
	
	/**
	 * @return the {@link TrackDAO}
	 */
	public TrackDAO getTrackDAO() {
		if (this.internetAccessProvider.isConnected()) {
			return new RemoteTrackDAO();
		}
		return new CacheTrackDAO();
	}
	
	/**
	 * @return the {@link UserDAO}
	 */
	public UserDAO getUserDAO() {
		if (this.internetAccessProvider.isConnected()) {
			return new RemoteUserDAO();
		}
		return new CacheUserDAO();
	}

}
