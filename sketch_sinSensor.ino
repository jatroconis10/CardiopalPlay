/* Sensor de ritmo cardiaco 
 * ------------------ 
*/
 #include <string.h>

 // variable para guardar el valor del sensor de ritmo cardiaco.
 int ritmo = 0;
 int presionSistolica = 0;
 int presionDiastolica = 0;
 int estres = 0;

 // arreglo de chars para envio final del dato del sensor.
 String heartRate = "";
 String bloodArray[2] = {"",""};
 String stress = "";
 
 // variable temporal de conteo
 int i = 0;
 
 // preparacion del proceso
 void setup() { 
   // Abre puerto serial y lo configura a 9600 bps
  Serial.begin(9600);
 }
 // ejecuta el procesamiento del sensor
 void loop() {
   ritmo = random(30,160);
   presionSistolica = random(70,190);
   double resta = presionSistolica/2.2;
   presionDiastolica = (int)(presionSistolica - resta);
   heartRate = String(ritmo);
   bloodArray[0] = String(presionSistolica);
   bloodArray[1] = String(presionDiastolica);

   if(ritmo < 60)
   {
    estres = 1;
   }
   else if(ritmo > 120)
   {
    estres = 3;
   }
   else
   {
    estres = 2;
   }
   stress = String(estres);
   // Envia los datos uno por uno del arreglo del sensor por puerto serial
   Serial.print(heartRate);
   Serial.print("\t");
    Serial.print(bloodArray[0]);
    Serial.print("\t");
    Serial.print(bloodArray[1]);
    Serial.print("\t");
   Serial.println(stress);
    // espera 1 segundo para repetir el procedimiento
   delay(1000);
 }
