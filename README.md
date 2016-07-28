# Pictorial

Pictorial is a universal image viewer for Android. It will open any image/gif url and load it directly instead of having to making you view it in the browser. It displays the image in an overlay by using a service so that the current foreground application is not paused. 

The images are loaded over a dimmed background so that you are still able to see any updates to the foreground application. e.g. seeing new incoming messages. 

<img src="http://i.imgur.com/fmNAfDd.png" width="226">

## Building

As the Imgur API is used, you will have to <a href="https://imgur.com/account/settings/apps">register a new Imgur application</a> to obtain your own `CLIENT_ID`. Once you have your `CLIENT_ID` you need to add it to your `gradle.properties` file like so:

```gradle
API_CLIENT_ID = "YOUR_CLIENT_ID_GOES_HERE"
```

## Supported URLs
* Direct image URLs
* Direct gif URLs
* Imgur URLs
* Gfycat URLs


License
-------

    Pictorial: A universal image viewer for Android
    Copyright (C) 2016  Jonas Bleyl

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
