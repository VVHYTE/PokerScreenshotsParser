# Mass-screenshots parser for PokerMaster

The path to the folder with screenshots is passed via the application's command-line argument (```/path/to/screenshots/```).

Screenshot sample:

<img src="https://camo.githubusercontent.com/4475cbb1115f2cb47279a2744948816dcfb43eb4/68747470733a2f2f692e696d6775722e636f6d2f6246416d4a45542e706e67" alt="Screenshot example" data-canonical-src="https://i.imgur.com/bFAmJET.png" width="350px">

Result: ```3cJc9dAh```

### How it works

Each PNG image in the folder is divided into monochrome areas that are alternately compared with pre-generated images of the values of suits and ranks of cards. The value is determined based on the compatibility principle. Which sample has the least difference with the original, then its value is assigned. The difference is calculated by comparing the sample with the original, and iterating over each pixel to see if it matches.


Configurable parsing areas:

<img src="https://camo.githubusercontent.com/df4bef0e5d1ad6cbfc982c8e19f1a807f378f173/68747470733a2f2f692e696d6775722e636f6d2f3450674a476d6e2e706e67" alt="Configurable parsing regions" data-canonical-src="https://i.imgur.com/4PgJGmn.png" width="550px">

Originals array with images, difference limits and values.

<img src="https://camo.githubusercontent.com/f4f95fd83ba1080a4dceff453ff4aa867a18af9b/68747470733a2f2f692e696d6775722e636f6d2f437561433843522e706e67" alt="asd" data-canonical-src="https://i.imgur.com/CuaC8CR.png" width="550px">

Counting difference mechanism:

<img src="https://camo.githubusercontent.com/32e40d2411b532a618f7e2e4c9696382a24cd10c/68747470733a2f2f692e696d6775722e636f6d2f524f474c756f522e706e67" alt="sd" data-canonical-src="https://i.imgur.com/ROGLuoR.png" width="550px">
