/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2020, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.tools.commandline.state;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.logging.Level;

import schemacrawler.SchemaCrawlerLogger;
import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.FilterOptions;
import schemacrawler.schemacrawler.GrepOptions;
import schemacrawler.schemacrawler.LimitOptions;
import schemacrawler.schemacrawler.LoadOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaRetrievalOptions;
import schemacrawler.tools.options.Config;

public class SchemaCrawlerShellState {

  private static final SchemaCrawlerLogger LOGGER =
      SchemaCrawlerLogger.getLogger(SchemaCrawlerShellState.class.getName());

  private Config config;
  private Catalog catalog;
  private Supplier<Connection> dataSource;
  private Throwable lastException;
  private SchemaCrawlerOptions schemaCrawlerOptions;
  private SchemaRetrievalOptions schemaRetrievalOptions;

  public void disconnect() {
    dataSource = null;
  }

  public Catalog getCatalog() {
    return catalog;
  }

  public Config getConfig() {
    return config;
  }

  public Supplier<Connection> getDataSource() {
    return dataSource;
  }

  public Throwable getLastException() {
    return lastException;
  }

  public SchemaCrawlerOptions getSchemaCrawlerOptions() {
    return schemaCrawlerOptions;
  }

  public SchemaRetrievalOptions getSchemaRetrievalOptions() {
    return schemaRetrievalOptions;
  }

  public boolean isConnected() {
    if (dataSource == null) {
      return false;
    }
    try (final Connection connection = dataSource.get()) {
      if (!connection.isValid(0)) {
        throw new SQLException("Connection is not valid");
      }
    } catch (final NullPointerException | SQLException e) {
      LOGGER.log(Level.WARNING, e.getMessage(), e);
      return false;
    }

    return true;
  }

  public boolean isLoaded() {
    return catalog != null;
  }

  public void setCatalog(final Catalog catalog) {
    this.catalog = catalog;
  }

  public void setConfig(final Config baseConfiguration) {
    if (baseConfiguration != null) {
      this.config = baseConfiguration;
    } else {
      this.config = new Config();
    }
  }

  public void setDataSource(final Supplier<Connection> dataSource) {
    this.dataSource = dataSource;
  }

  public void setLastException(final Throwable lastException) {
    this.lastException = lastException;
  }

  public void setSchemaCrawlerOptions(SchemaCrawlerOptions schemaCrawlerOptions) {
    this.schemaCrawlerOptions = schemaCrawlerOptions;
  }

  public void setSchemaRetrievalOptions(final SchemaRetrievalOptions schemaRetrievalOptions) {
    this.schemaRetrievalOptions = schemaRetrievalOptions;
  }

  public void sweep() {
    catalog = null;
    config = null;
    schemaCrawlerOptions = null;
    schemaRetrievalOptions = null;
    lastException = null;

    disconnect();
  }

  /** Update SchemaCrawler options by reassignment. */
  public void withFilterOptions(final FilterOptions filterOptions) {
    schemaCrawlerOptions = schemaCrawlerOptions.withFilterOptions(filterOptions);
  }

  /** Update SchemaCrawler options by reassignment. */
  public void withGrepOptions(final GrepOptions grepOptions) {
    schemaCrawlerOptions = schemaCrawlerOptions.withGrepOptions(grepOptions);
  }

  /** Update SchemaCrawler options by reassignment. */
  public void withLimitOptions(final LimitOptions limitOptions) {
    schemaCrawlerOptions = schemaCrawlerOptions.withLimitOptions(limitOptions);
  }

  /** Update SchemaCrawler options by reassignment. */
  public void withLoadOptions(final LoadOptions loadOptions) {
    schemaCrawlerOptions = schemaCrawlerOptions.withLoadOptions(loadOptions);
  }
}
