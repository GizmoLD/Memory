import 'package:client_flutter/app_data.dart';
import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';
import 'app_data.dart';
import 'widget_selectable_list.dart';

class LayoutPlaying extends StatefulWidget {
  const LayoutPlaying({Key? key}) : super(key: key);

  @override
  State<LayoutPlaying> createState() => _LayoutPlayingState();
}

class _LayoutPlayingState extends State<LayoutPlaying> {
  @override
  Widget build(BuildContext context) {
    AppData appData = Provider.of<AppData>(context);
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        middle: Text("Playing Screen"),
      ),
      child: GridView.builder(
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 4,
          crossAxisSpacing: 8.0,
          mainAxisSpacing: 8.0,
        ),
        itemCount: 16,
        itemBuilder: (BuildContext context, int index) {
          return CupertinoButton(
            onPressed: () {
              // Acción a realizar cuando se presiona el botón
              print("Botón $index presionado");
              // Puedes agregar más lógica aquí según sea necesario
            },
            color: CupertinoColors.activeBlue,
            child:
                Container(), // Puedes personalizar el contenido del botón aquí
          );
        },
      ),
    );
  }
}
