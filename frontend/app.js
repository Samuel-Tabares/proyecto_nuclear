// Configuración de la API
const API_URL = 'http://localhost:8080/api';

// Cambiar sección activa
function cambiarSeccion(seccion) {
    const contenido = document.getElementById('contenido');

    switch(seccion) {
        case 'propietarios':
            mostrarPropietarios(contenido);
            break;
        case 'mascotas':
            mostrarMascotas(contenido);
            break;
        case 'citas':
            mostrarCitas(contenido);
            break;
        case 'historias':
            mostrarHistorias(contenido);
            break;
        case 'prescripciones':
            mostrarPrescripciones(contenido);
            break;
        case 'facturas':
            mostrarFacturas(contenido);
            break;
    }
}

// Mostrar mensajes
function mostrarMensaje(mensaje, tipo = 'exito') {
    const div = document.createElement('div');
    div.className = `mensaje ${tipo}`;
    div.textContent = mensaje;

    const contenido = document.getElementById('contenido');
    contenido.insertBefore(div, contenido.firstChild);

    setTimeout(() => div.remove(), 5000);
}

// === PROPIETARIOS ===
async function mostrarPropietarios(contenido) {
    contenido.innerHTML = `
        <h2>Gestión de Propietarios</h2>
        <div class="formulario">
            <h3>Registrar Propietario</h3>
            <form id="formPropietario">
                <div class="form-group">
                    <label>Nombre:</label>
                    <input type="text" name="nombre" required>
                </div>
                <div class="form-group">
                    <label>Apellido:</label>
                    <input type="text" name="apellido" required>
                </div>
                <div class="form-group">
                    <label>Documento:</label>
                    <input type="text" name="documento" required>
                </div>
                <div class="form-group">
                    <label>Teléfono:</label>
                    <input type="text" name="telefono" required>
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" required>
                </div>
                <div class="form-group">
                    <label>Dirección:</label>
                    <input type="text" name="direccion">
                </div>
                <button type="submit" class="btn">Registrar</button>
            </form>
        </div>
        <div class="lista" id="listaPropietarios"></div>
    `;

    // Cargar lista
    cargarPropietarios();

    // Manejar envío
    document.getElementById('formPropietario').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);

        try {
            const response = await fetch(`${API_URL}/propietarios`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                mostrarMensaje('Propietario registrado exitosamente');
                e.target.reset();
                cargarPropietarios();
            } else {
                const error = await response.json();
                mostrarMensaje(error.message || 'Error al registrar', 'error');
            }
        } catch (error) {
            mostrarMensaje('Error de conexión', 'error');
        }
    });
}

async function cargarPropietarios() {
    try {
        const response = await fetch(`${API_URL}/propietarios`);
        const result = await response.json();
        const propietarios = result.data;

        const lista = document.getElementById('listaPropietarios');
        lista.innerHTML = '<h3>Propietarios Registrados</h3>';

        propietarios.forEach(p => {
            lista.innerHTML += `
                <div class="item">
                    <h4>${p.nombre} ${p.apellido}</h4>
                    <p><strong>Doc:</strong> ${p.documento}</p>
                    <p><strong>Tel:</strong> ${p.telefono}</p>
                    <p><strong>Email:</strong> ${p.email}</p>
                    <p><strong>ID:</strong> ${p.id}</p>
                </div>
            `;
        });
    } catch (error) {
        console.error('Error cargando propietarios:', error);
    }
}

// === MASCOTAS ===
async function mostrarMascotas(contenido) {
    contenido.innerHTML = `
        <h2>Gestión de Mascotas</h2>
        <div class="formulario">
            <h3>Registrar Mascota</h3>
            <form id="formMascota">
                <div class="form-group">
                    <label>ID Propietario:</label>
                    <input type="number" name="propietarioId" required>
                </div>
                <div class="form-group">
                    <label>Nombre:</label>
                    <input type="text" name="nombre" required>
                </div>
                <div class="form-group">
                    <label>Especie:</label>
                    <select name="especie" required>
                        <option value="Perro">Perro</option>
                        <option value="Gato">Gato</option>
                        <option value="Ave">Ave</option>
                        <option value="Conejo">Conejo</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Raza:</label>
                    <input type="text" name="raza">
                </div>
                <div class="form-group">
                    <label>Sexo:</label>
                    <select name="sexo">
                        <option value="Macho">Macho</option>
                        <option value="Hembra">Hembra</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Peso (kg):</label>
                    <input type="number" step="0.01" name="peso">
                </div>
                <button type="submit" class="btn">Registrar</button>
            </form>
        </div>
        <div class="lista" id="listaMascotas"></div>
    `;

    cargarMascotas();

    document.getElementById('formMascota').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.propietarioId = parseInt(data.propietarioId);
        data.peso = data.peso ? parseFloat(data.peso) : null;

        try {
            const response = await fetch(`${API_URL}/mascotas`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                mostrarMensaje('Mascota registrada exitosamente');
                e.target.reset();
                cargarMascotas();
            } else {
                const error = await response.json();
                mostrarMensaje(error.message || 'Error al registrar', 'error');
            }
        } catch (error) {
            mostrarMensaje('Error de conexión', 'error');
        }
    });
}

async function cargarMascotas() {
    try {
        const response = await fetch(`${API_URL}/mascotas`);
        const result = await response.json();
        const mascotas = result.data;

        const lista = document.getElementById('listaMascotas');
        lista.innerHTML = '<h3>Mascotas Registradas</h3>';

        mascotas.forEach(m => {
            lista.innerHTML += `
                <div class="item">
                    <h4>${m.nombre} (${m.especie})</h4>
                    <p><strong>Propietario:</strong> ${m.propietarioNombre}</p>
                    <p><strong>Raza:</strong> ${m.raza || 'N/A'}</p>
                    <p><strong>Peso:</strong> ${m.peso || 'N/A'} kg</p>
                    <p><strong>ID:</strong> ${m.id}</p>
                </div>
            `;
        });
    } catch (error) {
        console.error('Error cargando mascotas:', error);
    }
}

// === CITAS ===
async function mostrarCitas(contenido) {
    contenido.innerHTML = `
        <h2>Gestión de Citas</h2>
        <div class="formulario">
            <h3>Agendar Cita (con notificación automática)</h3>
            <form id="formCita">
                <div class="form-group">
                    <label>ID Mascota:</label>
                    <input type="number" name="mascotaId" required>
                </div>
                <div class="form-group">
                    <label>Fecha y Hora:</label>
                    <input type="datetime-local" name="fechaHora" required>
                </div>
                <div class="form-group">
                    <label>Motivo:</label>
                    <input type="text" name="motivo" required>
                </div>
                <div class="form-group">
                    <label>Observaciones:</label>
                    <textarea name="observaciones" rows="3"></textarea>
                </div>
                <button type="submit" class="btn">Agendar y Notificar</button>
            </form>
        </div>
        <div class="lista" id="listaCitas"></div>
    `;

    cargarCitas();

    document.getElementById('formCita').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.mascotaId = parseInt(data.mascotaId);

        try {
            const response = await fetch(`${API_URL}/citas`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                mostrarMensaje('Cita agendada y notificación enviada');
                e.target.reset();
                cargarCitas();
            } else {
                const error = await response.json();
                mostrarMensaje(error.message || 'Error al agendar', 'error');
            }
        } catch (error) {
            mostrarMensaje('Error de conexión', 'error');
        }
    });
}

async function cargarCitas() {
    try {
        const response = await fetch(`${API_URL}/citas`);
        const result = await response.json();
        const citas = result.data;

        const lista = document.getElementById('listaCitas');
        lista.innerHTML = '<h3>Citas Agendadas</h3>';

        citas.forEach(c => {
            lista.innerHTML += `
                <div class="item">
                    <h4>${c.mascotaNombre} - ${c.propietarioNombre}</h4>
                    <p><strong>Fecha:</strong> ${c.fechaHora}</p>
                    <p><strong>Motivo:</strong> ${c.motivo}</p>
                    <p><strong>Estado:</strong> ${c.estado}</p>
                    <p><strong>Email enviado a:</strong> ${c.propietarioEmail}</p>
                </div>
            `;
        });
    } catch (error) {
        console.error('Error cargando citas:', error);
    }
}

// === HISTORIAS CLÍNICAS ===
async function mostrarHistorias(contenido) {
    contenido.innerHTML = `
        <h2>Historias Clínicas</h2>
        <div class="formulario">
            <h3>Crear Historia Clínica</h3>
            <form id="formHistoria">
                <div class="form-group">
                    <label>ID Mascota:</label>
                    <input type="number" name="mascotaId" required>
                </div>
                <div class="form-group">
                    <label>Fecha Consulta:</label>
                    <input type="datetime-local" name="fechaConsulta" required>
                </div>
                <div class="form-group">
                    <label>Diagnóstico:</label>
                    <input type="text" name="diagnostico" required>
                </div>
                <div class="form-group">
                    <label>Síntomas:</label>
                    <textarea name="sintomas" rows="3"></textarea>
                </div>
                <div class="form-group">
                    <label>Tratamiento:</label>
                    <textarea name="tratamiento" rows="3"></textarea>
                </div>
                <div class="form-group">
                    <label>Peso (kg):</label>
                    <input type="number" step="0.01" name="pesoRegistrado">
                </div>
                <div class="form-group">
                    <label>Temperatura (°C):</label>
                    <input type="number" step="0.1" name="temperatura">
                </div>
                <button type="submit" class="btn">Crear</button>
            </form>
        </div>
        <div class="lista" id="listaHistorias"></div>
    `;

    cargarHistorias();

    document.getElementById('formHistoria').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.mascotaId = parseInt(data.mascotaId);
        data.pesoRegistrado = data.pesoRegistrado ? parseFloat(data.pesoRegistrado) : null;
        data.temperatura = data.temperatura ? parseFloat(data.temperatura) : null;

        try {
            const response = await fetch(`${API_URL}/historias`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                mostrarMensaje('Historia clínica creada');
                e.target.reset();
                cargarHistorias();
            } else {
                const error = await response.json();
                mostrarMensaje(error.message || 'Error al crear', 'error');
            }
        } catch (error) {
            mostrarMensaje('Error de conexión', 'error');
        }
    });
}

async function cargarHistorias() {
    try {
        const response = await fetch(`${API_URL}/historias`);
        const result = await response.json();
        const historias = result.data;

        const lista = document.getElementById('listaHistorias');
        lista.innerHTML = '<h3>Historias Clínicas</h3>';

        historias.forEach(h => {
            lista.innerHTML += `
                <div class="item">
                    <h4>${h.mascotaNombre} - ${h.propietarioNombre}</h4>
                    <p><strong>Diagnóstico:</strong> ${h.diagnostico}</p>
                    <p><strong>Fecha:</strong> ${h.fechaConsulta}</p>
                    <p><strong>ID:</strong> ${h.id}</p>
                </div>
            `;
        });
    } catch (error) {
        console.error('Error cargando historias:', error);
    }
}

// === PRESCRIPCIONES ===
async function mostrarPrescripciones(contenido) {
    contenido.innerHTML = `
        <h2>Prescripciones</h2>
        <div class="formulario">
            <h3>Crear Prescripción</h3>
            <form id="formPrescripcion">
                <div class="form-group">
                    <label>ID Mascota:</label>
                    <input type="number" name="mascotaId" required>
                </div>
                <div class="form-group">
                    <label>Medicamento:</label>
                    <input type="text" name="medicamento" required>
                </div>
                <div class="form-group">
                    <label>Dosis:</label>
                    <input type="text" name="dosis" required placeholder="Ej: 10mg">
                </div>
                <div class="form-group">
                    <label>Frecuencia:</label>
                    <input type="text" name="frecuencia" required placeholder="Ej: Cada 8 horas">
                </div>
                <div class="form-group">
                    <label>Duración (días):</label>
                    <input type="number" name="duracionDias" required>
                </div>
                <div class="form-group">
                    <label>Fecha Inicio:</label>
                    <input type="date" name="fechaInicio" required>
                </div>
                <div class="form-group">
                    <label>Indicaciones:</label>
                    <textarea name="indicaciones" rows="3"></textarea>
                </div>
                <button type="submit" class="btn">Crear</button>
            </form>
        </div>
        <div class="lista" id="listaPrescripciones"></div>
    `;

    cargarPrescripciones();

    document.getElementById('formPrescripcion').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.mascotaId = parseInt(data.mascotaId);
        data.duracionDias = parseInt(data.duracionDias);

        try {
            const response = await fetch(`${API_URL}/prescripciones`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                mostrarMensaje('Prescripción creada');
                e.target.reset();
                cargarPrescripciones();
            } else {
                const error = await response.json();
                mostrarMensaje(error.message || 'Error al crear', 'error');
            }
        } catch (error) {
            mostrarMensaje('Error de conexión', 'error');
        }
    });
}

async function cargarPrescripciones() {
    try {
        const response = await fetch(`${API_URL}/prescripciones`);
        const result = await response.json();
        const prescripciones = result.data;

        const lista = document.getElementById('listaPrescripciones');
        lista.innerHTML = '<h3>Prescripciones</h3>';

        prescripciones.forEach(p => {
            lista.innerHTML += `
                <div class="item">
                    <h4>${p.medicamento} - ${p.mascotaNombre}</h4>
                    <p><strong>Dosis:</strong> ${p.dosis} | <strong>Frecuencia:</strong> ${p.frecuencia}</p>
                    <p><strong>Duración:</strong> ${p.duracionDias} días</p>
                    <p><strong>Propietario:</strong> ${p.propietarioNombre}</p>
                </div>
            `;
        });
    } catch (error) {
        console.error('Error cargando prescripciones:', error);
    }
}

// === FACTURAS ===
async function mostrarFacturas(contenido) {
    contenido.innerHTML = `
        <h2>Facturación</h2>
        <div class="formulario">
            <h3>Crear Factura</h3>
            <form id="formFactura">
                <div class="form-group">
                    <label>ID Propietario:</label>
                    <input type="number" name="propietarioId" required>
                </div>
                <div class="form-group">
                    <label>ID Mascota:</label>
                    <input type="number" name="mascotaId" required>
                </div>
                <div class="form-group">
                    <label>Observaciones:</label>
                    <textarea name="observaciones" rows="2"></textarea>
                </div>
                <h4>Detalle de Servicios</h4>
                <div id="detallesContainer">
                    <div class="detalle-item">
                        <input type="text" placeholder="Descripción" name="desc_0" required>
                        <input type="number" placeholder="Cantidad" name="cant_0" required>
                        <input type="number" step="0.01" placeholder="Precio" name="precio_0" required>
                    </div>
                </div>
                <button type="button" class="btn btn-secondary" onclick="agregarDetalle()">+ Agregar Item</button>
                <button type="submit" class="btn">Crear Factura</button>
            </form>
        </div>
        <div class="lista" id="listaFacturas"></div>
    `;

    cargarFacturas();

    document.getElementById('formFactura').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            propietarioId: parseInt(formData.get('propietarioId')),
            mascotaId: parseInt(formData.get('mascotaId')),
            observaciones: formData.get('observaciones'),
            detalles: []
        };

        // Obtener detalles
        let i = 0;
        while (formData.get(`desc_${i}`) !== null) {
            data.detalles.push({
                descripcion: formData.get(`desc_${i}`),
                cantidad: parseInt(formData.get(`cant_${i}`)),
                precioUnitario: parseFloat(formData.get(`precio_${i}`))
            });
            i++;
        }

        try {
            const response = await fetch(`${API_URL}/facturas`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                mostrarMensaje('Factura creada exitosamente');
                e.target.reset();
                cargarFacturas();
            } else {
                const error = await response.json();
                mostrarMensaje(error.message || 'Error al crear', 'error');
            }
        } catch (error) {
            mostrarMensaje('Error de conexión', 'error');
        }
    });
}

let detalleIndex = 1;
function agregarDetalle() {
    const container = document.getElementById('detallesContainer');
    const div = document.createElement('div');
    div.className = 'detalle-item';
    div.innerHTML = `
        <input type="text" placeholder="Descripción" name="desc_${detalleIndex}" required>
        <input type="number" placeholder="Cantidad" name="cant_${detalleIndex}" required>
        <input type="number" step="0.01" placeholder="Precio" name="precio_${detalleIndex}" required>
    `;
    container.appendChild(div);
    detalleIndex++;
}

async function cargarFacturas() {
    try {
        const response = await fetch(`${API_URL}/facturas`);
        const result = await response.json();
        const facturas = result.data;

        const lista = document.getElementById('listaFacturas');
        lista.innerHTML = '<h3>Facturas Emitidas</h3>';

        facturas.forEach(f => {
            lista.innerHTML += `
                <div class="item">
                    <h4>Factura ${f.numeroFactura}</h4>
                    <p><strong>Propietario:</strong> ${f.propietarioNombre}</p>
                    <p><strong>Mascota:</strong> ${f.mascotaNombre}</p>
                    <p><strong>Subtotal:</strong> $${f.subtotal}</p>
                    <p><strong>IVA:</strong> ${f.impuesto}%</p>
                    <p><strong>Total:</strong> $${f.total}</p>
                    <p><strong>Estado:</strong> ${f.estado}</p>
                </div>
            `;
        });
    } catch (error) {
        console.error('Error cargando facturas:', error);
    }
}