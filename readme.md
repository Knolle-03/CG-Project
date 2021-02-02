# Base Framework for "Einführung in die Computergrafik"

## Install

IntelliJ Import -> Gradle project

## General usage

### Main Application (used for all exercises) 

* `wpcg.CG3D`
* for each exercise:
    * create Scene = class implementing interface `wpcg.base.scene`
* set scene in `wpcg.CG3D.main()`
* Camera
    * Rotate: left mouse button press + move
    * Zoom: right mouse button + move

### 2D Main Application: 

* `wpcg.MainFrame`
* with example content `wpcg.base.canvas2d.Canvas2D` 
            
