/*
 * Copyright (c) 2024, donnie <donnie4w@gmail.com> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * github.com/donnie4w/jdao
 */

package io.github.donnie4w.jdao.mapper;

import io.github.donnie4w.jdao.handle.DBType;
import io.github.donnie4w.jdao.handle.DBhandle;
import io.github.donnie4w.jdao.handle.JdaoException;

import javax.sql.DataSource;

/**
 *  JdaoMapper serves as the central access point within the Jdao framework for mapping SQL configurations defined in XML files to Java objects.
 *  This class facilitates the execution of Create, Read, Update, and Delete (CRUD) operations based on the identifiers (MapperId) specified in these configurations
 */
public abstract class JdaoMapper implements Mapper{

    /**
     * Default constructor for JdaoMapper.
     */
    protected JdaoMapper() {
    }

    /**
     * Builds the JDAO mapper using the specified XML path.
     *
     * @param xmlpath the path to the XML configuration file
     * @throws JdaoException if there is an error building the mapper
     */
    public static void build(String xmlpath) throws JdaoException {
        MapperParser.Mapper(xmlpath);
    }

    /**
     * Creates a new instance of JdaoMapper.
     *
     * @return a new instance of JdaoMapper
     */
    public static JdaoMapper newInstance() {
        return new MapperHandler();
    }

    /**
     * Uses the specified DB handle.
     *
     * @param dBhandle the DB handle to use
     * @return the JdaoMapper instance
     */
    public abstract JdaoMapper useDBhandle(DBhandle dBhandle);

    /**
     * Uses the specified DataSource and DB type.
     *
     * @param dataSource the DataSource to use
     * @param dbType the type of the database
     * @return the JdaoMapper instance
     */
    public abstract JdaoMapper useDBhandle(DataSource dataSource, DBType dbType);

    /**
     * the sql configure the mapping to the specified interface
     *
     * @param <T> the type of the mapper
     * @param clazz the class of the mapper
     * @return the mapper instance
     */
    public abstract <T> T getMapper(Class<T> clazz);
}
