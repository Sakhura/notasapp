# notasapp
# NotasApp 📝

Una aplicación Android nativa para crear, editar, buscar y eliminar notas, desarrollada en Kotlin con arquitectura clean y navegación entre Activities.

## 📱 Características

- ✅ **Crear nuevas notas** con título y contenido
- ✅ **Editar notas existentes** 
- ✅ **Eliminar notas** con confirmación
- ✅ **Buscar notas** por título o contenido
- ✅ **Interfaz intuitiva** con Material Design
- ✅ **Guardado automático** y manual
- ✅ **Lista ordenada** por fecha de creación

## 🏗️ Arquitectura

```
app/src/main/java/com/sakhura/notasapp/
├── MainActivity.kt              # Pantalla principal con lista
├── DetalleNotaActivity.kt       # Pantalla de edición/detalle
├── model/
│   └── Nota.kt                 # Clase de datos
├── adapter/
│   └── NotasAdapter.kt         # Adaptador RecyclerView
└── data/
    └── NotasManager.kt         # Gestor de datos (Singleton)
```

## 🚀 Tutorial: Crear desde Cero

### Paso 1: Crear Proyecto Android

1. **Abrir Android Studio**
2. **Crear nuevo proyecto:**
   - Template: `Empty Views Activity`
   - Name: `NotasApp`
   - Package: `com.sakhura.notasapp`
   - Language: `Kotlin`
   - Minimum SDK: `API 24`

### Paso 2: Configurar build.gradle.kts

```kotlin
// app/build.gradle.kts
android {
    namespace = "com.sakhura.notasapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sakhura.notasapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        viewBinding = true  // ← IMPORTANTE: Habilitar ViewBinding
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Dependencias para testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

### Paso 3: Crear el Modelo de Datos

**Crear carpeta:** `app/src/main/java/com/sakhura/notasapp/model/`

```kotlin
// model/Nota.kt
package com.sakhura.notasapp.model

data class Nota(
    val id: Long,
    var titulo: String,
    var contenido: String,
    val fechaCreacion: Long = System.currentTimeMillis()
)
```

### Paso 4: Crear el Gestor de Datos

**Crear carpeta:** `app/src/main/java/com/sakhura/notasapp/data/`

```kotlin
// data/NotasManager.kt
package com.sakhura.notasapp.data

import com.sakhura.notasapp.model.Nota

object NotasManager {
    // Lista de notas en memoria
    private val notas = mutableListOf(
        Nota(System.currentTimeMillis(), "Nota de ejemplo", "Este es el contenido de prueba.")
    )

    // Obtener todas las notas ordenadas por fecha (más recientes primero)
    fun obtenerNotas(): List<Nota> = notas.sortedByDescending { it.fechaCreacion }

    // Agregar una nueva nota
    fun agregarNota(nota: Nota) {
        notas.add(nota)
    }

    // Eliminar una nota por ID
    fun eliminarNota(id: Long) {
        notas.removeIf { it.id == id }
    }

    // Buscar notas por título o contenido
    fun buscarNotas(query: String): List<Nota> {
        return notas.filter { 
            it.titulo.contains(query, ignoreCase = true) || 
            it.contenido.contains(query, ignoreCase = true)
        }.sortedByDescending { it.fechaCreacion }
    }

    // Obtener una nota específica por su ID
    fun obtenerNotaPorId(id: Long): Nota? {
        return notas.find { it.id == id }
    }

    // Actualizar una nota existente
    fun actualizarNota(nuevaNota: Nota) {
        val index = notas.indexOfFirst { it.id == nuevaNota.id }
        if (index != -1) {
            notas[index] = nuevaNota
        }
    }
}
```

### Paso 5: Crear Layouts

#### 5.1 Layout Principal (activity_main.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="16dp">

        <!-- Buscador -->
        <androidx.appcompat.widget.SearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:queryHint="@string/buscar"
            android:iconifiedByDefault="false" />

        <!-- Lista de notas -->
        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rvNotas"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:paddingTop="8dp"
            android:clipToPadding="false" />

    </LinearLayout>

    <!-- Botón flotante para nueva nota -->
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabAgregarNota"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="16dp"
        android:contentDescription="@string/desc_fab_agregar"
        app:srcCompat="@android:drawable/ic_input_add" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

#### 5.2 Layout del Item de Nota (item_nota.xml)

**Crear archivo:** `app/src/main/res/layout/item_nota.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card_view="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    card_view:cardCornerRadius="8dp"
    card_view:cardElevation="4dp"
    android:layout_margin="8dp"
    android:foreground="?android:attr/selectableItemBackground">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:id="@+id/tvTitulo"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Título de Nota"
            android:textSize="18sp"
            android:textStyle="bold"
            android:textColor="@color/gray_dark" />

        <TextView
            android:id="@+id/tvFecha"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Fecha"
            android:textSize="14sp"
            android:textColor="@color/gray_dark"
            android:layout_marginTop="4dp" />
    </LinearLayout>
</androidx.cardview.widget.CardView>
```

#### 5.3 Layout de Detalle (activity_detalle_nota.xml)

**Crear archivo:** `app/src/main/res/layout/activity_detalle_nota.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".DetalleNotaActivity">

    <!-- Campo de título -->
    <EditText
        android:id="@+id/etTitulo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Título de la nota"
        android:textSize="18sp"
        android:textStyle="bold"
        android:background="@android:color/transparent"
        android:padding="8dp"
        android:inputType="text"
        android:maxLines="2" />

    <!-- Línea separadora -->
    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#E0E0E0"
        android:layout_marginTop="8dp"
        android:layout_marginBottom="8dp" />

    <!-- Campo de contenido -->
    <EditText
        android:id="@+id/etContenido"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:hint="Escribe tu nota aquí..."
        android:gravity="top"
        android:background="@android:color/transparent"
        android:padding="8dp"
        android:textSize="16sp"
        android:inputType="textMultiLine"
        android:overScrollMode="ifContentScrolls" />

    <!-- Contenedor de botones -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginTop="16dp">

        <Button
            android:id="@+id/btnGuardar"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="Guardar"
            android:textColor="@android:color/white"
            android:backgroundTint="@color/purple_500"
            android:layout_marginEnd="8dp" />

        <Button
            android:id="@+id/btnEliminar"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="Eliminar"
            android:textColor="@android:color/white"
            android:backgroundTint="@android:color/holo_red_light"
            android:layout_marginStart="8dp" />

    </LinearLayout>

</LinearLayout>
```

### Paso 6: Configurar Recursos

#### 6.1 Colores (colors.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_500">#6200EE</color>
    <color name="purple_700">#3700B3</color>
    <color name="teal_200">#03DAC5</color>
    <color name="teal_700">#018786</color>
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>
    <color name="gray_light">#F5F5F5</color>
    <color name="gray_dark">#212121</color>
    <color name="red_error">#B00020</color>
</resources>
```

#### 6.2 Cadenas (strings.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">NotasApp</string>
    <string name="nueva_nota">Nueva Nota</string>
    <string name="titulo">Título</string>
    <string name="contenido">Contenido</string>
    <string name="eliminar">Eliminar</string>
    <string name="guardar">Guardar</string>
    <string name="buscar">Buscar notas...</string>
    <string name="desc_fab_agregar">Agregar nueva nota</string>
</resources>
```

### Paso 7: Crear el Adaptador

**Crear carpeta:** `app/src/main/java/com/sakhura/notasapp/adapter/`

```kotlin
// adapter/NotasAdapter.kt
package com.sakhura.notasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.notasapp.R
import com.sakhura.notasapp.model.Nota
import java.text.SimpleDateFormat
import java.util.*

class NotasAdapter(
    private val onNotaClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private val notas = mutableListOf<Nota>()

    fun actualizarNotas(nuevasNotas: List<Nota>) {
        notas.clear()
        notas.addAll(nuevasNotas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.bind(notas[position])
    }

    override fun getItemCount(): Int = notas.size

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nota: Nota) {
            val tvTitulo = itemView.findViewById<TextView>(R.id.tvTitulo)
            val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)

            // Mostrar el título o "Sin título" si está vacío
            tvTitulo.text = if (nota.titulo.isNotEmpty()) nota.titulo else "Sin título"
            
            // Formatear y mostrar la fecha
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvFecha.text = sdf.format(Date(nota.fechaCreacion))

            itemView.setOnClickListener {
                onNotaClick(nota)
            }
        }
    }
}
```

### Paso 8: Implementar MainActivity

```kotlin
// MainActivity.kt
package com.sakhura.notasapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakhura.notasapp.adapter.NotasAdapter
import com.sakhura.notasapp.data.NotasManager
import com.sakhura.notasapp.databinding.ActivityMainBinding
import com.sakhura.notasapp.model.Nota

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar adaptador
        adapter = NotasAdapter { nota ->
            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nota.id)
            startActivity(intent)
        }

        // Configurar RecyclerView
        binding.rvNotas.layoutManager = LinearLayoutManager(this)
        binding.rvNotas.adapter = adapter

        // Botón flotante para nueva nota
        binding.fabAgregarNota.setOnClickListener {
            val nuevaNotaId = System.currentTimeMillis()
            val nuevaNota = Nota(nuevaNotaId, "", "")
            
            // Guardar nota temporal
            NotasManager.agregarNota(nuevaNota)
            
            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nuevaNota.id)
            startActivity(intent)
        }

        // Configurar búsqueda
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtradas = NotasManager.buscarNotas(newText.orEmpty())
                adapter.actualizarNotas(filtradas)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Refrescar lista al volver del detalle
        adapter.actualizarNotas(NotasManager.obtenerNotas())
    }
}
```

### Paso 9: Implementar DetalleNotaActivity

```kotlin
// DetalleNotaActivity.kt
package com.sakhura.notasapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sakhura.notasapp.data.NotasManager
import com.sakhura.notasapp.databinding.ActivityDetalleNotaBinding
import com.sakhura.notasapp.model.Nota

class DetalleNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNotaBinding
    private var nota: Nota? = null
    private var notaOriginalVacia = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idNota = intent.getLongExtra("nota_id", -1)
        nota = NotasManager.obtenerNotaPorId(idNota)

        if (nota != null) {
            notaOriginalVacia = nota!!.titulo.isEmpty() && nota!!.contenido.isEmpty()
            
            binding.etTitulo.setText(nota!!.titulo)
            binding.etContenido.setText(nota!!.contenido)
        } else {
            finish()
            return
        }

        // Botón guardar
        binding.btnGuardar.setOnClickListener {
            guardarNota()
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Botón eliminar
        binding.btnEliminar.setOnClickListener {
            nota?.let {
                NotasManager.eliminarNota(it.id)
                Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun guardarNota() {
        val titulo = binding.etTitulo.text.toString().trim()
        val contenido = binding.etContenido.text.toString().trim()

        nota?.let { notaActual ->
            if (titulo.isNotEmpty() || contenido.isNotEmpty()) {
                notaActual.titulo = titulo
                notaActual.contenido = contenido
                NotasManager.actualizarNota(notaActual)
            } else if (notaOriginalVacia) {
                NotasManager.eliminarNota(notaActual.id)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        guardarNota()
    }
}
```

### Paso 10: Configurar AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Notasapp"
        tools:targetApi="31">
        
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <activity
            android:name=".DetalleNotaActivity"
            android:exported="false" />
            
    </application>

</manifest>
```

## 🛠️ Pasos Finales

### 1. Sincronizar el proyecto
```bash
Build > Clean Project
Build > Rebuild Project
```

### 2. Ejecutar la aplicación
- Conectar dispositivo Android o iniciar emulador
- Click en el botón "Run" (▶️) en Android Studio

## 📁 Estructura Final del Proyecto

```
NotasApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/sakhura/notasapp/
│   │   │   ├── MainActivity.kt
│   │   │   ├── DetalleNotaActivity.kt
│   │   │   ├── model/
│   │   │   │   └── Nota.kt
│   │   │   ├── adapter/
│   │   │   │   └── NotasAdapter.kt
│   │   │   └── data/
│   │   │       └── NotasManager.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_detalle_nota.xml
│   │   │   │   └── item_nota.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
└── build.gradle.kts
```

## 🎯 Funcionalidades Implementadas

- ✅ **CRUD completo**: Crear, leer, actualizar y eliminar notas
- ✅ **Búsqueda en tiempo real**: Por título y contenido
- ✅ **Interfaz Material Design**: Moderna y atractiva
- ✅ **Navegación fluida**: Entre activities
- ✅ **Guardado automático y manual**: Doble seguridad
- ✅ **Gestión de estado**: Datos persistentes en memoria
- ✅ **ViewBinding**: Acceso seguro a vistas

## 🚀 Posibles Mejoras Futuras

- 💾 **Persistencia**: SQLite o Room Database
- 🔄 **Sincronización**: Firebase o API REST
- 🎨 **Temas**: Modo oscuro/claro
- 📱 **Responsive**: Adaptación a tablets
- 🔍 **Búsqueda avanzada**: Filtros y categorías
- 📤 **Exportar/Importar**: Backup de notas
- 🔒 **Seguridad**: Cifrado de notas sensibles

---

## 👨‍💻 Desarrollado por

**Sakhura** - [GitHub](https://github.com/Sakhura/notasapp)

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.
