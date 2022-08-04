- Region.createRegion is called (when region gets loaded, controlled by server)

- - Region goes through all 114x114 tiles (int x = -5; x < this.regionSizeX + 5; x++)

- - it blends some colours together based on neighbours

- - `if (underlayFloorId > 0 || overlayFloorId > 0)`,      tile gets rendered

- - - worldController.renderTile(int plane, int x, int y, ..., ...) [public]

- - a tile is either a PlainTile or a ShapedTile

- - - PlainTiles are a simple tile with 6 vertices (2 triangles) and are blended with 4 colours at the corners

- - - ShapedTiles are made up of many vertices and are used for tiles with specific shapes (e.g. paths)

- WorldController.render is called (every frame)

- - it figures out which tiles are visible (~25 tile render distance)

- - for all visible tiles, it calls `this.renderTile(tile, flag)` [private]

- - this will then render the plane tile, or shaped tile, if present (prefers plain tile if both present)

- - `this.renderPlainTile(groundTile.plainTile, x, y, l, curveSineX, curveCosineX, curveSineY, curveCosineY);`

- - The tile is then rasterized as triangles onto the game canvas

- - The renderer then moves on to other objects on the tile (wall objects, decorations, etc)