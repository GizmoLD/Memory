import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pantayator/appdata.dart';
import 'package:provider/provider.dart';

class PlayingRoom extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // Accede directamente a AppData
    AppData appData = Provider.of<AppData>(context);

    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        middle: Text('Sala de Juego'),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 40.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text('Turno de: X'),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text('En espera: Y'),
                ),
              ],
            ),
          ),
          Expanded(
            child: Center(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: GridView.builder(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 4,
                    mainAxisSpacing: 8.0,
                    crossAxisSpacing: 8.0,
                  ),
                  itemCount: appData.fruits.length,
                  itemBuilder: (BuildContext context, int index) {
                    return Container(
                      width: 50,
                      height: 50,
                      child: CupertinoButton(
                        onPressed: () {
                          print('Botón $index presionado');
                        },
                        //color: getFruitColor(appData.fruits[index].fruit),
                        color: getFruitColor(appData.fruits[index].fruit),
                        child: Text(""),
                      ),
                    );
                  },
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Color getFruitColor(String fruit) {
    switch (fruit) {
      // CupertinoColors.activeBlue
      case 'fresa':
        return CupertinoColors.systemRed;
      case 'naranja':
        return CupertinoColors.activeOrange;
      case 'sandia':
        return CupertinoColors.systemPink;
      case 'kiwi':
        return CupertinoColors.activeGreen;
      case 'uva':
        return CupertinoColors.systemPurple;
      case 'platano':
        return CupertinoColors.systemYellow;
      case 'pera':
        return CupertinoColors.systemBrown;
      case 'manzana':
        return CupertinoColors.black;
      default:
        return CupertinoColors.activeBlue;
    }
  }
}
