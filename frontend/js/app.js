/* =========================================================
   SkyRegister - Lógica de interacción del Front-End
   Los datos de esta versión demostrativa se guardan en localStorage.
   En producción, estas funciones deben consumir la API del backend.
   ========================================================= */

"use strict";

// Datos iniciales de demostración para visualizar los módulos.
const datosIniciales = {
    drones: [
        { id: 1, modelo: "DJI Mavic 3 Enterprise", serial: "DJI-M3E-001", estado: "Disponible" },
        { id: 2, modelo: "DJI Matrice 300 RTK", serial: "DJI-M300-002", estado: "En operación" },
        { id: 3, modelo: "DJI Air 3", serial: "DJI-A3-003", estado: "Mantenimiento" },
        { id: 4, modelo: "DJI Mini 4 Pro", serial: "DJI-M4P-004", estado: "Disponible" }
    ],
    flights: [
        { id: 101, codigo: "SKY-001", cliente: "Constructora Andina", dron: "DJI Mavic 3 Enterprise", fecha: "2026-11-02", estado: "Completado" },
        { id: 102, codigo: "SKY-002", cliente: "Inmobiliaria Norte", dron: "DJI Matrice 300 RTK", fecha: "2026-11-05", estado: "Programado" },
        { id: 103, codigo: "SKY-003", cliente: "Agro Valle", dron: "DJI Air 3", fecha: "2026-11-08", estado: "En revisión" },
        { id: 104, codigo: "SKY-004", cliente: "Constructora Andina", dron: "DJI Mini 4 Pro", fecha: "2026-11-10", estado: "Programado" }
    ],
    clients: [
        { id: 201, nombre: "Constructora Andina", correo: "contacto@andina.com", telefono: "601 555 0101" },
        { id: 202, nombre: "Inmobiliaria Norte", correo: "proyectos@norte.com", telefono: "601 555 0102" },
        { id: 203, nombre: "Agro Valle", correo: "operaciones@agrovalle.com", telefono: "602 555 0103" }
    ]
};

// Recupera los datos guardados o carga los registros de ejemplo.
function cargarDatos() {
    try {
        const guardados = localStorage.getItem("skyRegisterData");
        return guardados ? JSON.parse(guardados) : structuredClone(datosIniciales);
    } catch (error) {
        console.error("No fue posible recuperar los datos locales:", error);
        return structuredClone(datosIniciales);
    }
}

let datos = cargarDatos();
let tipoRegistroActual = "";

// Persiste los cambios realizados en el navegador.
function guardarDatos() {
    localStorage.setItem("skyRegisterData", JSON.stringify(datos));
}

// Cambia la vista activa y actualiza el nombre de la sección.
function navegarA(vista) {
    document.querySelectorAll(".view").forEach((elemento) => elemento.classList.remove("active"));
    document.querySelectorAll(".nav-link").forEach((elemento) => elemento.classList.toggle("active", elemento.dataset.view === vista));

    const vistaSeleccionada = document.getElementById(`view-${vista}`);
    if (!vistaSeleccionada) return;

    vistaSeleccionada.classList.add("active");
    const enlace = document.querySelector(`.nav-link[data-view="${vista}"]`);
    document.getElementById("breadcrumb-current").textContent = enlace ? enlace.textContent.trim() : "Panel principal";
    document.getElementById("sidebar").classList.remove("open");
    renderizarTodo();
}

// Construye una etiqueta visual según el estado del registro.
function crearBadge(estado) {
    const clases = {
        "Disponible": "success",
        "Completado": "success",
        "Programado": "info",
        "En operación": "info",
        "En revisión": "warning",
        "Mantenimiento": "danger"
    };
    return `<span class="badge ${clases[estado] || "info"}">${estado}</span>`;
}

// Muestra los drones en la tabla y aplica el filtro de búsqueda.
function renderizarDrones(filtro = "") {
    const tabla = document.getElementById("drone-table");
    if (!tabla) return;
    const encontrados = datos.drones.filter((dron) =>
        `${dron.modelo} ${dron.serial}`.toLowerCase().includes(filtro.toLowerCase())
    );
    tabla.innerHTML = encontrados.length ? encontrados.map((dron) => `
        <tr>
            <td><strong>${escaparHTML(dron.modelo)}</strong></td>
            <td>${escaparHTML(dron.serial)}</td>
            <td>${crearBadge(dron.estado)}</td>
            <td><button class="action-button delete" data-delete="drones" data-id="${dron.id}">Eliminar</button></td>
        </tr>`).join("") : '<tr><td colspan="4" class="empty-state">No se encontraron drones.</td></tr>';
}

// Muestra los vuelos y permite buscar por código o cliente.
function renderizarVuelos(filtro = "") {
    const tabla = document.getElementById("flight-table");
    if (!tabla) return;
    const encontrados = datos.flights.filter((vuelo) =>
        `${vuelo.codigo} ${vuelo.cliente}`.toLowerCase().includes(filtro.toLowerCase())
    );
    tabla.innerHTML = encontrados.length ? encontrados.map((vuelo) => `
        <tr>
            <td><strong>${escaparHTML(vuelo.codigo)}</strong></td>
            <td>${escaparHTML(vuelo.cliente)}</td>
            <td>${escaparHTML(vuelo.dron)}</td>
            <td>${escaparHTML(vuelo.fecha)}</td>
            <td>${crearBadge(vuelo.estado)}</td>
            <td><button class="action-button delete" data-delete="flights" data-id="${vuelo.id}">Eliminar</button></td>
        </tr>`).join("") : '<tr><td colspan="6" class="empty-state">No se encontraron vuelos.</td></tr>';
}

// Muestra los clientes registrados.
function renderizarClientes(filtro = "") {
    const tabla = document.getElementById("client-table");
    if (!tabla) return;
    const encontrados = datos.clients.filter((cliente) =>
        `${cliente.nombre} ${cliente.correo}`.toLowerCase().includes(filtro.toLowerCase())
    );
    tabla.innerHTML = encontrados.length ? encontrados.map((cliente) => `
        <tr>
            <td><strong>${escaparHTML(cliente.nombre)}</strong></td>
            <td>${escaparHTML(cliente.correo)}</td>
            <td>${escaparHTML(cliente.telefono)}</td>
            <td><button class="action-button delete" data-delete="clients" data-id="${cliente.id}">Eliminar</button></td>
        </tr>`).join("") : '<tr><td colspan="4" class="empty-state">No se encontraron clientes.</td></tr>';
}

// Actualiza indicadores y resumen de vuelos.
function renderizarDashboard() {
    document.getElementById("stat-drones").textContent = datos.drones.length;
    document.getElementById("stat-flights").textContent = datos.flights.length;
    document.getElementById("stat-clients").textContent = datos.clients.length;
    document.getElementById("stat-reports").textContent = Math.max(18, datos.flights.length);
    const recientes = [...datos.flights].slice(-4).reverse();
    document.getElementById("recent-flights").innerHTML = recientes.map((vuelo) => `
        <tr><td><strong>${escaparHTML(vuelo.codigo)}</strong></td><td>${escaparHTML(vuelo.cliente)}</td>
        <td>${escaparHTML(vuelo.fecha)}</td><td>${crearBadge(vuelo.estado)}</td></tr>`).join("");
}

// Presenta un gráfico sencillo construido con CSS, sin dependencias externas.
function renderizarReportes() {
    document.getElementById("report-drones").textContent = datos.drones.length;
    document.getElementById("report-flights").textContent = datos.flights.length;
    document.getElementById("report-clients").textContent = datos.clients.length;
    const estados = ["Completado", "Programado", "En revisión"];
    const contenedor = document.getElementById("flight-bars");
    contenedor.innerHTML = estados.map((estado) => {
        const cantidad = datos.flights.filter((vuelo) => vuelo.estado === estado).length;
        const porcentaje = datos.flights.length ? (cantidad / datos.flights.length) * 100 : 0;
        return `<div class="bar-row"><span>${estado}</span><div class="bar-track"><div class="bar-fill" style="width:${porcentaje}%"></div></div><b>${cantidad}</b></div>`;
    }).join("");
}

// Actualiza todas las secciones que dependen de los datos.
function renderizarTodo() {
    renderizarDashboard();
    renderizarDrones(document.getElementById("drone-search")?.value || "");
    renderizarVuelos(document.getElementById("flight-search")?.value || "");
    renderizarClientes(document.getElementById("client-search")?.value || "");
    renderizarReportes();
}

// Evita que texto ingresado por el usuario se interprete como HTML.
function escaparHTML(valor) {
    return String(valor).replace(/[&<>"']/g, (caracter) => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#039;"
    })[caracter]);
}

// Define los campos de cada formulario y abre la ventana modal.
function abrirFormulario(tipo) {
    tipoRegistroActual = tipo;
    const configuracion = {
        drones: { titulo: "Registrar dron", campos: [
            { id: "modelo", etiqueta: "Modelo del dron", tipo: "text", requerido: true },
            { id: "serial", etiqueta: "Número de serie", tipo: "text", requerido: true },
            { id: "estado", etiqueta: "Estado", tipo: "select", opciones: ["Disponible", "En operación", "Mantenimiento"] }
        ]},
        flights: { titulo: "Programar vuelo", campos: [
            { id: "codigo", etiqueta: "Código de vuelo", tipo: "text", requerido: true },
            { id: "cliente", etiqueta: "Cliente", tipo: "text", requerido: true },
            { id: "dron", etiqueta: "Modelo del dron", tipo: "text", requerido: true },
            { id: "fecha", etiqueta: "Fecha de operación", tipo: "date", requerido: true },
            { id: "estado", etiqueta: "Estado", tipo: "select", opciones: ["Programado", "Completado", "En revisión"] }
        ]},
        clients: { titulo: "Registrar cliente", campos: [
            { id: "nombre", etiqueta: "Nombre o razón social", tipo: "text", requerido: true },
            { id: "correo", etiqueta: "Correo electrónico", tipo: "email", requerido: true },
            { id: "telefono", etiqueta: "Teléfono", tipo: "tel", requerido: true }
        ]}
    };
    const ajuste = configuracion[tipo];
    document.getElementById("modal-title").textContent = ajuste.titulo;
    document.getElementById("form-fields").innerHTML = ajuste.campos.map((campo) => {
        const control = campo.tipo === "select"
            ? `<select class="form-control" id="${campo.id}" required>${campo.opciones.map((opcion) => `<option>${opcion}</option>`).join("")}</select>`
            : `<input class="form-control" id="${campo.id}" type="${campo.tipo}" ${campo.requerido ? "required" : ""}>`;
        return `<div class="form-group"><label for="${campo.id}">${campo.etiqueta}</label>${control}</div>`;
    }).join("");
    document.getElementById("modal-backdrop").classList.add("open");
    document.getElementById("modal-backdrop").setAttribute("aria-hidden", "false");
}

// Cierra la ventana modal y reinicia el formulario.
function cerrarFormulario() {
    document.getElementById("modal-backdrop").classList.remove("open");
    document.getElementById("modal-backdrop").setAttribute("aria-hidden", "true");
    document.getElementById("record-form").reset();
}

// Guarda el nuevo registro en la colección local correspondiente.
function guardarRegistro(evento) {
    evento.preventDefault();
    const formulario = evento.currentTarget;
    if (!formulario.reportValidity()) return;

    const registro = {};
    formulario.querySelectorAll(".form-control").forEach((campo) => {
        registro[campo.id] = campo.value.trim();
    });

    registro.id = Date.now();
    datos[tipoRegistroActual].push(registro);
    guardarDatos();
    cerrarFormulario();
    renderizarTodo();
    mostrarMensaje("Registro guardado correctamente.");
}

// Elimina un registro después de confirmar la acción.
function eliminarRegistro(coleccion, id) {
    if (!confirm("¿Deseas eliminar este registro?")) return;
    datos[coleccion] = datos[coleccion].filter((registro) => registro.id !== Number(id));
    guardarDatos();
    renderizarTodo();
    mostrarMensaje("Registro eliminado.");
}

// Informa al usuario sobre el resultado de una acción.
function mostrarMensaje(mensaje) {
    const toast = document.getElementById("toast");
    toast.textContent = mensaje;
    toast.classList.add("show");
    window.setTimeout(() => toast.classList.remove("show"), 2600);
}

// Exporta la información de vuelos en formato CSV.
function exportarReporte() {
    const encabezados = ["Codigo", "Cliente", "Dron", "Fecha", "Estado"];
    const filas = datos.flights.map((vuelo) =>
        [vuelo.codigo, vuelo.cliente, vuelo.dron, vuelo.fecha, vuelo.estado]
    );
    const contenido = [encabezados, ...filas].map((fila) =>
        fila.map((valor) => `"${String(valor).replace(/"/g, '""')}"`).join(";")
    ).join("\n");
    const archivo = new Blob(["\uFEFF" + contenido], { type: "text/csv;charset=utf-8;" });
    const enlace = document.createElement("a");
    enlace.href = URL.createObjectURL(archivo);
    enlace.download = "reporte-vuelos-skyregister.csv";
    enlace.click();
    URL.revokeObjectURL(enlace.href);
    mostrarMensaje("Reporte exportado correctamente.");
}

// Registra los eventos de navegación, formularios, filtros y acciones.
function inicializarAplicacion() {
    document.getElementById("today-label").textContent = new Intl.DateTimeFormat("es-CO", {
        day: "numeric", month: "long", year: "numeric"
    }).format(new Date());

    document.addEventListener("click", (evento) => {
        const navegacion = evento.target.closest("[data-view]");
        const accion = evento.target.closest("[data-action]");
        const eliminar = evento.target.closest("[data-delete]");

        if (navegacion) navegarA(navegacion.dataset.view);
        if (accion) {
            if (accion.dataset.action === "new-drone") abrirFormulario("drones");
            if (accion.dataset.action === "new-flight") abrirFormulario("flights");
            if (accion.dataset.action === "new-client") abrirFormulario("clients");
        }
        if (eliminar) eliminarRegistro(eliminar.dataset.delete, eliminar.dataset.id);
    });

    document.getElementById("record-form").addEventListener("submit", guardarRegistro);
    document.getElementById("modal-close").addEventListener("click", cerrarFormulario);
    document.getElementById("cancel-modal").addEventListener("click", cerrarFormulario);
    document.getElementById("modal-backdrop").addEventListener("click", (evento) => {
        if (evento.target.id === "modal-backdrop") cerrarFormulario();
    });
    document.getElementById("menu-toggle").addEventListener("click", () =>
        document.getElementById("sidebar").classList.toggle("open")
    );
    document.getElementById("profile-shortcut").addEventListener("click", () => navegarA("profile"));
    document.getElementById("drone-search").addEventListener("input", (evento) => renderizarDrones(evento.target.value));
    document.getElementById("flight-search").addEventListener("input", (evento) => renderizarVuelos(evento.target.value));
    document.getElementById("client-search").addEventListener("input", (evento) => renderizarClientes(evento.target.value));
    document.getElementById("export-report").addEventListener("click", exportarReporte);

    renderizarTodo();
}

document.addEventListener("DOMContentLoaded", inicializarAplicacion);
