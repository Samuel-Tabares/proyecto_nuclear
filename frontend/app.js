// ========================================
// VetApp - Frontend JavaScript
// ========================================

const API_URL = 'http://localhost:8080/api';

// Estado global
let propietarios = [];
let mascotas = [];
let citas = [];
let historias = [];
let prescripciones = [];
let facturas = [];

// ========================================
// Inicialización
// ========================================

document.addEventListener('DOMContentLoaded', () => {
    cambiarSeccion('inicio');
    actualizarNavegacion('inicio');
});

// ========================================
// Navegación
// ========================================

function cambiarSeccion(seccion) {
    const contenido = document.getElementById('contenido');
    actualizarNavegacion(seccion);

    switch(seccion) {
        case 'inicio':
            mostrarInicio(contenido);
            break;
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

function actualizarNavegacion(seccionActiva) {
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.textContent.toLowerCase().includes(seccionActiva.substring(0, 4))) {
            btn.classList.add('active');
        }
    });
    // Fix para inicio
    if (seccionActiva === 'inicio') {
        document.querySelector('.nav-btn').classList.add('active');
    }
}

// ========================================
// Utilidades
// ========================================

function mostrarMensaje(mensaje, tipo = 'exito') {
    const contenido = document.getElementById('contenido');
    const mensajeExistente = contenido.querySelector('.mensaje');
    if (mensajeExistente) mensajeExistente.remove();

    const div = document.createElement('div');
    div.className = `mensaje ${tipo}`;
    div.innerHTML = `
        <span>${tipo === 'exito' ? '✅' : tipo === 'error' ? '❌' : 'ℹ️'}</span>
        <span>${mensaje}</span>
    `;

    const seccion = contenido.querySelector('.seccion');
    if (seccion) {
        seccion.insertBefore(div, seccion.firstChild);
    }

    setTimeout(() => div.remove(), 5000);
}

function mostrarLoading(container) {
    container.innerHTML = `
        <div class="loading">
            <div class="spinner"></div>
        </div>
    `;
}

function formatearFecha(fecha) {
    if (!fecha) return 'N/A';
    return new Date(fecha).toLocaleDateString('es-CO', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatearMoneda(valor) {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0
    }).format(valor);
}

async function fetchAPI(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_URL}${endpoint}`, {
            headers: { 'Content-Type': 'application/json' },
            ...options
        });
        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Error en la solicitud');
        }
        return data;
    } catch (error) {
        console.error('Error API:', error);
        throw error;
    }
}

// ========================================
// INICIO / DASHBOARD
// ========================================

async function mostrarInicio(contenido) {
    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>🏠 Panel de Control</h2>
            </div>
            
            <div class="dashboard" id="dashboard-stats">
                <div class="stat-card">
                    <h3 id="stat-propietarios">-</h3>
                    <p>Propietarios</p>
                </div>
                <div class="stat-card">
                    <h3 id="stat-mascotas">-</h3>
                    <p>Mascotas</p>
                </div>
                <div class="stat-card">
                    <h3 id="stat-citas">-</h3>
                    <p>Citas Agendadas</p>
                </div>
                <div class="stat-card">
                    <h3 id="stat-facturas">-</h3>
                    <p>Facturas</p>
                </div>
            </div>

            <div class="features-grid">
                <div class="feature-card">
                    <h3>✅ Funcionalidades</h3>
                    <ul>
                        <li>Registro de propietarios</li>
                        <li>Gestión de mascotas</li>
                        <li>Agendamiento de citas con notificación</li>
                        <li>Historias clínicas</li>
                        <li>Prescripción de medicamentos</li>
                        <li>Facturación con envío por email</li>
                    </ul>
                </div>
                <div class="feature-card">
                    <h3>🏗️ Arquitectura</h3>
                    <ul>
                        <li>Arquitectura Modular</li>
                        <li>Patrones: Repository, Service, Facade, Strategy, Factory</li>
                        <li>API REST con Spring Boot</li>
                        <li>Base de datos MySQL</li>
                        <li>Notificaciones por Email</li>
                        <li>Dockerizado</li>
                    </ul>
                </div>
            </div>
        </div>
    `;

    // Cargar estadísticas
    try {
        const [propRes, mascRes, citaRes, factRes] = await Promise.all([
            fetchAPI('/propietarios'),
            fetchAPI('/mascotas'),
            fetchAPI('/citas'),
            fetchAPI('/facturas')
        ]);

        document.getElementById('stat-propietarios').textContent = propRes.data?.length || 0;
        document.getElementById('stat-mascotas').textContent = mascRes.data?.length || 0;
        document.getElementById('stat-citas').textContent = citaRes.data?.length || 0;
        document.getElementById('stat-facturas').textContent = factRes.data?.length || 0;
    } catch (error) {
        console.error('Error cargando estadísticas:', error);
    }
}

// ========================================
// PROPIETARIOS
// ========================================

async function mostrarPropietarios(contenido) {
    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>👤 Gestión de Propietarios</h2>
            </div>

            <div class="form-container">
                <h3>➕ Registrar Nuevo Propietario</h3>
                <form id="formPropietario">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Nombre *</label>
                            <input type="text" name="nombre" required placeholder="Ej: Juan">
                        </div>
                        <div class="form-group">
                            <label>Apellido *</label>
                            <input type="text" name="apellido" required placeholder="Ej: Pérez">
                        </div>
                        <div class="form-group">
                            <label>Documento *</label>
                            <input type="text" name="documento" required placeholder="Ej: 123456789">
                        </div>
                        <div class="form-group">
                            <label>Teléfono *</label>
                            <input type="text" name="telefono" required placeholder="Ej: 3001234567">
                        </div>
                        <div class="form-group">
                            <label>Email *</label>
                            <input type="email" name="email" required placeholder="Ej: juan@email.com">
                        </div>
                        <div class="form-group">
                            <label>Dirección</label>
                            <input type="text" name="direccion" placeholder="Ej: Calle 123 #45-67">
                        </div>
                    </div>
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">💾 Guardar Propietario</button>
                    </div>
                </form>
            </div>

            <h3>📋 Propietarios Registrados</h3>
            <div id="listaPropietarios" class="cards-grid"></div>
        </div>
    `;

    cargarPropietarios();

    document.getElementById('formPropietario').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);

        try {
            await fetchAPI('/propietarios', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            mostrarMensaje('Propietario registrado exitosamente');
            e.target.reset();
            cargarPropietarios();
        } catch (error) {
            mostrarMensaje(error.message, 'error');
        }
    });
}

async function cargarPropietarios() {
    const lista = document.getElementById('listaPropietarios');
    mostrarLoading(lista);

    try {
        const result = await fetchAPI('/propietarios');
        propietarios = result.data || [];

        if (propietarios.length === 0) {
            lista.innerHTML = `
                <div class="empty-state">
                    <h3>No hay propietarios registrados</h3>
                    <p>Registra el primer propietario usando el formulario de arriba</p>
                </div>
            `;
            return;
        }

        lista.innerHTML = propietarios.map(p => `
            <div class="card">
                <div class="card-header">
                    <h4>👤 ${p.nombre} ${p.apellido}</h4>
                    <span class="card-badge badge-info">ID: ${p.id}</span>
                </div>
                <div class="card-body">
                    <p><strong>📄 Documento:</strong> ${p.documento}</p>
                    <p><strong>📞 Teléfono:</strong> ${p.telefono}</p>
                    <p><strong>📧 Email:</strong> ${p.email}</p>
                    <p><strong>📍 Dirección:</strong> ${p.direccion || 'N/A'}</p>
                </div>
                <div class="card-footer">
                    <button class="btn btn-sm btn-danger" onclick="eliminarPropietario(${p.id})">🗑️ Eliminar</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        lista.innerHTML = `<div class="mensaje error">Error al cargar propietarios: ${error.message}</div>`;
    }
}

async function eliminarPropietario(id) {
    if (!confirm('¿Estás seguro de eliminar este propietario?')) return;

    try {
        await fetchAPI(`/propietarios/${id}`, { method: 'DELETE' });
        mostrarMensaje('Propietario eliminado');
        cargarPropietarios();
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

// ========================================
// MASCOTAS
// ========================================

async function mostrarMascotas(contenido) {
    // Cargar propietarios para el select
    try {
        const result = await fetchAPI('/propietarios');
        propietarios = result.data || [];
    } catch (error) {
        console.error('Error cargando propietarios:', error);
    }

    const propietariosOptions = propietarios.map(p =>
        `<option value="${p.id}">${p.nombre} ${p.apellido}</option>`
    ).join('');

    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>🐕 Gestión de Mascotas</h2>
            </div>

            <div class="form-container">
                <h3>➕ Registrar Nueva Mascota</h3>
                <form id="formMascota">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Propietario *</label>
                            <select name="propietarioId" required>
                                <option value="">Seleccionar propietario</option>
                                ${propietariosOptions}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Nombre *</label>
                            <input type="text" name="nombre" required placeholder="Ej: Max">
                        </div>
                        <div class="form-group">
                            <label>Especie *</label>
                            <select name="especie" required>
                                <option value="Perro">🐕 Perro</option>
                                <option value="Gato">🐈 Gato</option>
                                <option value="Ave">🐦 Ave</option>
                                <option value="Conejo">🐰 Conejo</option>
                                <option value="Otro">🐾 Otro</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Raza</label>
                            <input type="text" name="raza" placeholder="Ej: Labrador">
                        </div>
                        <div class="form-group">
                            <label>Sexo</label>
                            <select name="sexo">
                                <option value="Macho">Macho</option>
                                <option value="Hembra">Hembra</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Color</label>
                            <input type="text" name="color" placeholder="Ej: Dorado">
                        </div>
                        <div class="form-group">
                            <label>Peso (kg)</label>
                            <input type="number" step="0.1" name="peso" placeholder="Ej: 25.5">
                        </div>
                        <div class="form-group">
                            <label>Fecha de Nacimiento</label>
                            <input type="date" name="fechaNacimiento">
                        </div>
                    </div>
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">💾 Guardar Mascota</button>
                    </div>
                </form>
            </div>

            <h3>📋 Mascotas Registradas</h3>
            <div id="listaMascotas" class="cards-grid"></div>
        </div>
    `;

    cargarMascotas();

    document.getElementById('formMascota').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.propietarioId = parseInt(data.propietarioId);
        data.peso = data.peso ? parseFloat(data.peso) : null;

        try {
            await fetchAPI('/mascotas', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            mostrarMensaje('Mascota registrada exitosamente');
            e.target.reset();
            cargarMascotas();
        } catch (error) {
            mostrarMensaje(error.message, 'error');
        }
    });
}

async function cargarMascotas() {
    const lista = document.getElementById('listaMascotas');
    mostrarLoading(lista);

    try {
        const result = await fetchAPI('/mascotas');
        mascotas = result.data || [];

        if (mascotas.length === 0) {
            lista.innerHTML = `
                <div class="empty-state">
                    <h3>No hay mascotas registradas</h3>
                    <p>Registra la primera mascota usando el formulario de arriba</p>
                </div>
            `;
            return;
        }

        const especieEmoji = {
            'Perro': '🐕',
            'Gato': '🐈',
            'Ave': '🐦',
            'Conejo': '🐰',
            'Otro': '🐾'
        };

        lista.innerHTML = mascotas.map(m => `
            <div class="card">
                <div class="card-header">
                    <h4>${especieEmoji[m.especie] || '🐾'} ${m.nombre}</h4>
                    <span class="card-badge badge-success">ID: ${m.id}</span>
                </div>
                <div class="card-body">
                    <p><strong>👤 Propietario:</strong> ${m.propietarioNombre}</p>
                    <p><strong>🏷️ Especie:</strong> ${m.especie}</p>
                    <p><strong>🔖 Raza:</strong> ${m.raza || 'N/A'}</p>
                    <p><strong>⚖️ Peso:</strong> ${m.peso ? m.peso + ' kg' : 'N/A'}</p>
                    <p><strong>🎨 Color:</strong> ${m.color || 'N/A'}</p>
                </div>
                <div class="card-footer">
                    <button class="btn btn-sm btn-danger" onclick="eliminarMascota(${m.id})">🗑️ Eliminar</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        lista.innerHTML = `<div class="mensaje error">Error al cargar mascotas: ${error.message}</div>`;
    }
}

async function eliminarMascota(id) {
    if (!confirm('¿Estás seguro de eliminar esta mascota?')) return;

    try {
        await fetchAPI(`/mascotas/${id}`, { method: 'DELETE' });
        mostrarMensaje('Mascota eliminada');
        cargarMascotas();
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

// ========================================
// CITAS
// ========================================

async function mostrarCitas(contenido) {
    // Cargar mascotas para el select
    try {
        const result = await fetchAPI('/mascotas');
        mascotas = result.data || [];
    } catch (error) {
        console.error('Error cargando mascotas:', error);
    }

    const mascotasOptions = mascotas.map(m =>
        `<option value="${m.id}">${m.nombre} (${m.propietarioNombre})</option>`
    ).join('');

    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>📅 Gestión de Citas</h2>
            </div>

            <div class="mensaje info">
                📧 Al crear una cita, se enviará automáticamente una notificación por email al propietario.
            </div>

            <div class="form-container">
                <h3>➕ Agendar Nueva Cita</h3>
                <form id="formCita">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Mascota *</label>
                            <select name="mascotaId" required>
                                <option value="">Seleccionar mascota</option>
                                ${mascotasOptions}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Fecha y Hora *</label>
                            <input type="datetime-local" name="fechaHora" required>
                        </div>
                        <div class="form-group">
                            <label>Motivo *</label>
                            <input type="text" name="motivo" required placeholder="Ej: Vacunación anual">
                        </div>
                        <div class="form-group">
                            <label>Observaciones</label>
                            <textarea name="observaciones" placeholder="Notas adicionales..."></textarea>
                        </div>
                    </div>
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">📅 Agendar Cita</button>
                    </div>
                </form>
            </div>

            <h3>📋 Citas Agendadas</h3>
            <div id="listaCitas" class="cards-grid"></div>
        </div>
    `;

    cargarCitas();

    document.getElementById('formCita').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.mascotaId = parseInt(data.mascotaId);

        try {
            await fetchAPI('/citas', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            mostrarMensaje('Cita agendada exitosamente. Se ha enviado notificación por email.');
            e.target.reset();
            cargarCitas();
        } catch (error) {
            mostrarMensaje(error.message, 'error');
        }
    });
}

async function cargarCitas() {
    const lista = document.getElementById('listaCitas');
    mostrarLoading(lista);

    try {
        const result = await fetchAPI('/citas');
        citas = result.data || [];

        if (citas.length === 0) {
            lista.innerHTML = `
                <div class="empty-state">
                    <h3>No hay citas agendadas</h3>
                    <p>Agenda la primera cita usando el formulario de arriba</p>
                </div>
            `;
            return;
        }

        const estadoBadge = {
            'AGENDADA': 'badge-info',
            'CONFIRMADA': 'badge-success',
            'EN_CURSO': 'badge-warning',
            'COMPLETADA': 'badge-success',
            'CANCELADA': 'badge-danger'
        };

        lista.innerHTML = citas.map(c => `
            <div class="card">
                <div class="card-header">
                    <h4>📅 ${c.mascotaNombre}</h4>
                    <span class="card-badge ${estadoBadge[c.estado] || 'badge-info'}">${c.estado}</span>
                </div>
                <div class="card-body">
                    <p><strong>👤 Propietario:</strong> ${c.propietarioNombre}</p>
                    <p><strong>📧 Email:</strong> ${c.propietarioEmail}</p>
                    <p><strong>📆 Fecha:</strong> ${c.fechaHora}</p>
                    <p><strong>📝 Motivo:</strong> ${c.motivo}</p>
                    ${c.observaciones ? `<p><strong>💬 Observaciones:</strong> ${c.observaciones}</p>` : ''}
                </div>
                <div class="card-footer">
                    <button class="btn btn-sm btn-danger" onclick="eliminarCita(${c.id})">🗑️ Eliminar</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        lista.innerHTML = `<div class="mensaje error">Error al cargar citas: ${error.message}</div>`;
    }
}

async function eliminarCita(id) {
    if (!confirm('¿Estás seguro de eliminar esta cita?')) return;

    try {
        await fetchAPI(`/citas/${id}`, { method: 'DELETE' });
        mostrarMensaje('Cita eliminada');
        cargarCitas();
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

// ========================================
// HISTORIAS CLÍNICAS
// ========================================

async function mostrarHistorias(contenido) {
    try {
        const result = await fetchAPI('/mascotas');
        mascotas = result.data || [];
    } catch (error) {
        console.error('Error cargando mascotas:', error);
    }

    const mascotasOptions = mascotas.map(m =>
        `<option value="${m.id}">${m.nombre} (${m.propietarioNombre})</option>`
    ).join('');

    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>📋 Historias Clínicas</h2>
            </div>

            <div class="form-container">
                <h3>➕ Nueva Historia Clínica</h3>
                <form id="formHistoria">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Mascota *</label>
                            <select name="mascotaId" required>
                                <option value="">Seleccionar mascota</option>
                                ${mascotasOptions}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Fecha de Consulta *</label>
                            <input type="datetime-local" name="fechaConsulta" required>
                        </div>
                        <div class="form-group">
                            <label>Diagnóstico *</label>
                            <input type="text" name="diagnostico" required placeholder="Ej: Otitis leve">
                        </div>
                        <div class="form-group">
                            <label>Peso (kg)</label>
                            <input type="number" step="0.1" name="pesoRegistrado" placeholder="Ej: 25.5">
                        </div>
                        <div class="form-group">
                            <label>Temperatura (°C)</label>
                            <input type="number" step="0.1" name="temperatura" placeholder="Ej: 38.5">
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Síntomas</label>
                        <textarea name="sintomas" placeholder="Describe los síntomas observados..."></textarea>
                    </div>
                    <div class="form-group">
                        <label>Tratamiento</label>
                        <textarea name="tratamiento" placeholder="Tratamiento prescrito..."></textarea>
                    </div>
                    <div class="form-group">
                        <label>Observaciones</label>
                        <textarea name="observaciones" placeholder="Notas adicionales..."></textarea>
                    </div>
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">💾 Guardar Historia</button>
                    </div>
                </form>
            </div>

            <h3>📋 Historias Registradas</h3>
            <div id="listaHistorias" class="cards-grid"></div>
        </div>
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
            await fetchAPI('/historias', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            mostrarMensaje('Historia clínica creada exitosamente');
            e.target.reset();
            cargarHistorias();
        } catch (error) {
            mostrarMensaje(error.message, 'error');
        }
    });
}

async function cargarHistorias() {
    const lista = document.getElementById('listaHistorias');
    mostrarLoading(lista);

    try {
        const result = await fetchAPI('/historias');
        historias = result.data || [];

        if (historias.length === 0) {
            lista.innerHTML = `
                <div class="empty-state">
                    <h3>No hay historias clínicas</h3>
                    <p>Crea la primera historia usando el formulario de arriba</p>
                </div>
            `;
            return;
        }

        lista.innerHTML = historias.map(h => `
            <div class="card">
                <div class="card-header">
                    <h4>📋 ${h.mascotaNombre}</h4>
                    <span class="card-badge badge-info">ID: ${h.id}</span>
                </div>
                <div class="card-body">
                    <p><strong>👤 Propietario:</strong> ${h.propietarioNombre}</p>
                    <p><strong>📆 Fecha:</strong> ${h.fechaConsulta}</p>
                    <p><strong>🩺 Diagnóstico:</strong> ${h.diagnostico}</p>
                    ${h.pesoRegistrado ? `<p><strong>⚖️ Peso:</strong> ${h.pesoRegistrado} kg</p>` : ''}
                    ${h.temperatura ? `<p><strong>🌡️ Temperatura:</strong> ${h.temperatura}°C</p>` : ''}
                    ${h.sintomas ? `<p><strong>📝 Síntomas:</strong> ${h.sintomas}</p>` : ''}
                    ${h.tratamiento ? `<p><strong>💊 Tratamiento:</strong> ${h.tratamiento}</p>` : ''}
                </div>
                <div class="card-footer">
                    <button class="btn btn-sm btn-info" onclick="editarHistoria(${h.id})">✏️ Editar</button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarHistoria(${h.id})">🗑️ Eliminar</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        lista.innerHTML = `<div class="mensaje error">Error al cargar historias: ${error.message}</div>`;
    }
}

async function editarHistoria(id) {
    try {
        const result = await fetchAPI(`/historias/${id}`);
        const h = result.data;

        document.getElementById('modal-body').innerHTML = `
            <h3>✏️ Editar Historia Clínica</h3>
            <form id="formEditarHistoria">
                <input type="hidden" name="mascotaId" value="${h.mascotaId}">
                <div class="form-group">
                    <label>Fecha de Consulta *</label>
                    <input type="datetime-local" name="fechaConsulta" value="${h.fechaConsulta ? h.fechaConsulta.substring(0, 16) : ''}" required>
                </div>
                <div class="form-group">
                    <label>Diagnóstico *</label>
                    <input type="text" name="diagnostico" value="${h.diagnostico}" required>
                </div>
                <div class="form-group">
                    <label>Peso (kg)</label>
                    <input type="number" step="0.1" name="pesoRegistrado" value="${h.pesoRegistrado || ''}">
                </div>
                <div class="form-group">
                    <label>Temperatura (°C)</label>
                    <input type="number" step="0.1" name="temperatura" value="${h.temperatura || ''}">
                </div>
                <div class="form-group">
                    <label>Síntomas</label>
                    <textarea name="sintomas">${h.sintomas || ''}</textarea>
                </div>
                <div class="form-group">
                    <label>Tratamiento</label>
                    <textarea name="tratamiento">${h.tratamiento || ''}</textarea>
                </div>
                <div class="form-group">
                    <label>Observaciones</label>
                    <textarea name="observaciones">${h.observaciones || ''}</textarea>
                </div>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">💾 Guardar Cambios</button>
                    <button type="button" class="btn btn-secondary" onclick="cerrarModal()">Cancelar</button>
                </div>
            </form>
        `;

        document.getElementById('modal').classList.add('active');

        document.getElementById('formEditarHistoria').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const data = Object.fromEntries(formData);
            data.mascotaId = parseInt(data.mascotaId);
            data.pesoRegistrado = data.pesoRegistrado ? parseFloat(data.pesoRegistrado) : null;
            data.temperatura = data.temperatura ? parseFloat(data.temperatura) : null;

            try {
                await fetchAPI(`/historias/${id}`, {
                    method: 'PUT',
                    body: JSON.stringify(data)
                });
                mostrarMensaje('Historia clínica actualizada');
                cerrarModal();
                cargarHistorias();
            } catch (error) {
                mostrarMensaje(error.message, 'error');
            }
        });
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

async function eliminarHistoria(id) {
    if (!confirm('¿Estás seguro de eliminar esta historia clínica?')) return;

    try {
        await fetchAPI(`/historias/${id}`, { method: 'DELETE' });
        mostrarMensaje('Historia clínica eliminada');
        cargarHistorias();
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

// ========================================
// PRESCRIPCIONES
// ========================================

async function mostrarPrescripciones(contenido) {
    try {
        const result = await fetchAPI('/mascotas');
        mascotas = result.data || [];
    } catch (error) {
        console.error('Error cargando mascotas:', error);
    }

    const mascotasOptions = mascotas.map(m =>
        `<option value="${m.id}">${m.nombre} (${m.propietarioNombre})</option>`
    ).join('');

    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>💊 Prescripciones</h2>
            </div>

            <div class="form-container">
                <h3>➕ Nueva Prescripción</h3>
                <form id="formPrescripcion">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Mascota *</label>
                            <select name="mascotaId" required>
                                <option value="">Seleccionar mascota</option>
                                ${mascotasOptions}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Medicamento *</label>
                            <input type="text" name="medicamento" required placeholder="Ej: Amoxicilina 250mg">
                        </div>
                        <div class="form-group">
                            <label>Dosis *</label>
                            <input type="text" name="dosis" required placeholder="Ej: 1 tableta">
                        </div>
                        <div class="form-group">
                            <label>Frecuencia *</label>
                            <input type="text" name="frecuencia" required placeholder="Ej: Cada 12 horas">
                        </div>
                        <div class="form-group">
                            <label>Duración (días) *</label>
                            <input type="number" name="duracionDias" required placeholder="Ej: 7">
                        </div>
                        <div class="form-group">
                            <label>Fecha Inicio *</label>
                            <input type="date" name="fechaInicio" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Indicaciones</label>
                        <textarea name="indicaciones" placeholder="Instrucciones especiales..."></textarea>
                    </div>
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">💾 Guardar Prescripción</button>
                    </div>
                </form>
            </div>

            <h3>📋 Prescripciones Registradas</h3>
            <div id="listaPrescripciones" class="cards-grid"></div>
        </div>
    `;

    cargarPrescripciones();

    document.getElementById('formPrescripcion').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.mascotaId = parseInt(data.mascotaId);
        data.duracionDias = parseInt(data.duracionDias);

        try {
            await fetchAPI('/prescripciones', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            mostrarMensaje('Prescripción creada exitosamente');
            e.target.reset();
            cargarPrescripciones();
        } catch (error) {
            mostrarMensaje(error.message, 'error');
        }
    });
}

async function cargarPrescripciones() {
    const lista = document.getElementById('listaPrescripciones');
    mostrarLoading(lista);

    try {
        const result = await fetchAPI('/prescripciones');
        prescripciones = result.data || [];

        if (prescripciones.length === 0) {
            lista.innerHTML = `
                <div class="empty-state">
                    <h3>No hay prescripciones</h3>
                    <p>Crea la primera prescripción usando el formulario de arriba</p>
                </div>
            `;
            return;
        }

        lista.innerHTML = prescripciones.map(p => `
            <div class="card">
                <div class="card-header">
                    <h4>💊 ${p.medicamento}</h4>
                    <span class="card-badge badge-success">${p.duracionDias} días</span>
                </div>
                <div class="card-body">
                    <p><strong>🐾 Mascota:</strong> ${p.mascotaNombre}</p>
                    <p><strong>👤 Propietario:</strong> ${p.propietarioNombre}</p>
                    <p><strong>💉 Dosis:</strong> ${p.dosis}</p>
                    <p><strong>🕐 Frecuencia:</strong> ${p.frecuencia}</p>
                    <p><strong>📅 Inicio:</strong> ${p.fechaInicio}</p>
                    <p><strong>📅 Fin:</strong> ${p.fechaFin}</p>
                    ${p.indicaciones ? `<p><strong>📝 Indicaciones:</strong> ${p.indicaciones}</p>` : ''}
                </div>
                <div class="card-footer">
                    <button class="btn btn-sm btn-danger" onclick="eliminarPrescripcion(${p.id})">🗑️ Eliminar</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        lista.innerHTML = `<div class="mensaje error">Error al cargar prescripciones: ${error.message}</div>`;
    }
}

async function eliminarPrescripcion(id) {
    if (!confirm('¿Estás seguro de eliminar esta prescripción?')) return;

    try {
        await fetchAPI(`/prescripciones/${id}`, { method: 'DELETE' });
        mostrarMensaje('Prescripción eliminada');
        cargarPrescripciones();
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

// ========================================
// FACTURAS
// ========================================

let detalleIndex = 0;

async function mostrarFacturas(contenido) {
    try {
        const [propRes, mascRes] = await Promise.all([
            fetchAPI('/propietarios'),
            fetchAPI('/mascotas')
        ]);
        propietarios = propRes.data || [];
        mascotas = mascRes.data || [];
    } catch (error) {
        console.error('Error cargando datos:', error);
    }

    const propietariosOptions = propietarios.map(p =>
        `<option value="${p.id}">${p.nombre} ${p.apellido}</option>`
    ).join('');

    const mascotasOptions = mascotas.map(m =>
        `<option value="${m.id}" data-propietario="${m.propietarioId}">${m.nombre} (${m.propietarioNombre})</option>`
    ).join('');

    detalleIndex = 0;

    contenido.innerHTML = `
        <div class="seccion">
            <div class="seccion-header">
                <h2>🧾 Facturación</h2>
            </div>

            <div class="mensaje info">
                📧 Al crear una factura, se enviará automáticamente por email al propietario.
            </div>

            <div class="form-container">
                <h3>➕ Nueva Factura</h3>
                <form id="formFactura">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Propietario *</label>
                            <select name="propietarioId" id="selectPropietario" required>
                                <option value="">Seleccionar propietario</option>
                                ${propietariosOptions}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Mascota *</label>
                            <select name="mascotaId" id="selectMascota" required>
                                <option value="">Seleccionar mascota</option>
                                ${mascotasOptions}
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Observaciones</label>
                        <textarea name="observaciones" placeholder="Notas adicionales..."></textarea>
                    </div>

                    <h4 style="margin: 20px 0 15px;">📦 Detalle de Servicios/Productos</h4>
                    <div class="detalles-container" id="detallesContainer">
                        <div class="detalle-item" id="detalle-0">
                            <input type="text" placeholder="Descripción *" name="desc_0" required>
                            <input type="number" placeholder="Cantidad" name="cant_0" value="1" min="1" required style="width: 100px;">
                            <input type="number" placeholder="Precio" name="precio_0" step="0.01" required style="width: 150px;">
                            <button type="button" class="btn btn-sm btn-danger" onclick="eliminarDetalle(0)" style="opacity: 0.5;" disabled>✕</button>
                        </div>
                    </div>
                    <button type="button" class="add-detalle-btn" onclick="agregarDetalle()">
                        ➕ Agregar otro item
                    </button>

                    <div class="btn-group" style="margin-top: 20px;">
                        <button type="submit" class="btn btn-primary">🧾 Crear Factura</button>
                    </div>
                </form>
            </div>

            <h3>📋 Facturas Emitidas</h3>
            <div id="listaFacturas" class="cards-grid"></div>
        </div>
    `;

    cargarFacturas();

    // Filtrar mascotas cuando se selecciona propietario
    document.getElementById('selectPropietario').addEventListener('change', (e) => {
        const propId = e.target.value;
        const selectMascota = document.getElementById('selectMascota');
        const options = selectMascota.querySelectorAll('option');

        options.forEach(opt => {
            if (opt.value === '') {
                opt.style.display = '';
            } else if (!propId || opt.dataset.propietario === propId) {
                opt.style.display = '';
            } else {
                opt.style.display = 'none';
            }
        });
        selectMascota.value = '';
    });

    document.getElementById('formFactura').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            propietarioId: parseInt(formData.get('propietarioId')),
            mascotaId: parseInt(formData.get('mascotaId')),
            observaciones: formData.get('observaciones'),
            detalles: []
        };

        // Recoger detalles
        let i = 0;
        while (formData.get(`desc_${i}`) !== null) {
            const desc = formData.get(`desc_${i}`);
            const cant = formData.get(`cant_${i}`);
            const precio = formData.get(`precio_${i}`);

            if (desc && cant && precio) {
                data.detalles.push({
                    descripcion: desc,
                    cantidad: parseInt(cant),
                    precioUnitario: parseFloat(precio)
                });
            }
            i++;
        }

        if (data.detalles.length === 0) {
            mostrarMensaje('Agrega al menos un item a la factura', 'error');
            return;
        }

        try {
            await fetchAPI('/facturas', {
                method: 'POST',
                body: JSON.stringify(data)
            });
            mostrarMensaje('Factura creada y enviada por email exitosamente');
            e.target.reset();
            detalleIndex = 0;
            document.getElementById('detallesContainer').innerHTML = `
                <div class="detalle-item" id="detalle-0">
                    <input type="text" placeholder="Descripción *" name="desc_0" required>
                    <input type="number" placeholder="Cantidad" name="cant_0" value="1" min="1" required style="width: 100px;">
                    <input type="number" placeholder="Precio" name="precio_0" step="0.01" required style="width: 150px;">
                    <button type="button" class="btn btn-sm btn-danger" onclick="eliminarDetalle(0)" style="opacity: 0.5;" disabled>✕</button>
                </div>
            `;
            cargarFacturas();
        } catch (error) {
            mostrarMensaje(error.message, 'error');
        }
    });
}

function agregarDetalle() {
    detalleIndex++;
    const container = document.getElementById('detallesContainer');
    const div = document.createElement('div');
    div.className = 'detalle-item';
    div.id = `detalle-${detalleIndex}`;
    div.innerHTML = `
        <input type="text" placeholder="Descripción *" name="desc_${detalleIndex}" required>
        <input type="number" placeholder="Cantidad" name="cant_${detalleIndex}" value="1" min="1" required style="width: 100px;">
        <input type="number" placeholder="Precio" name="precio_${detalleIndex}" step="0.01" required style="width: 150px;">
        <button type="button" class="btn btn-sm btn-danger" onclick="eliminarDetalle(${detalleIndex})">✕</button>
    `;
    container.appendChild(div);
}

function eliminarDetalle(index) {
    const detalle = document.getElementById(`detalle-${index}`);
    if (detalle) {
        detalle.remove();
    }
}

async function cargarFacturas() {
    const lista = document.getElementById('listaFacturas');
    mostrarLoading(lista);

    try {
        const result = await fetchAPI('/facturas');
        facturas = result.data || [];

        if (facturas.length === 0) {
            lista.innerHTML = `
                <div class="empty-state">
                    <h3>No hay facturas</h3>
                    <p>Crea la primera factura usando el formulario de arriba</p>
                </div>
            `;
            return;
        }

        const estadoBadge = {
            'PENDIENTE': 'badge-warning',
            'PAGADA': 'badge-success',
            'CANCELADA': 'badge-danger'
        };

        lista.innerHTML = facturas.map(f => `
            <div class="card factura-card">
                <div class="card-header">
                    <h4>🧾 ${f.numeroFactura}</h4>
                    <span class="card-badge ${estadoBadge[f.estado] || 'badge-info'}">${f.estado}</span>
                </div>
                <div class="card-body">
                    <p><strong>👤 Propietario:</strong> ${f.propietarioNombre}</p>
                    <p><strong>🐾 Mascota:</strong> ${f.mascotaNombre}</p>
                    <p><strong>📅 Fecha:</strong> ${f.fechaEmision}</p>
                    <p><strong>📧 Email:</strong> ${f.propietarioEmail}</p>
                    
                    <div style="background: #f3f4f6; padding: 10px; border-radius: 8px; margin: 10px 0;">
                        <p><strong>Subtotal:</strong> ${formatearMoneda(f.subtotal)}</p>
                        <p><strong>IVA (${f.impuesto}%):</strong> ${formatearMoneda(f.subtotal * f.impuesto / 100)}</p>
                        <p class="factura-total"><strong>Total:</strong> ${formatearMoneda(f.total)}</p>
                    </div>
                    
                    ${f.observaciones ? `<p><strong>📝 Observaciones:</strong> ${f.observaciones}</p>` : ''}
                </div>
                <div class="card-footer">
                    <button class="btn btn-sm btn-info" onclick="verDetalleFactura(${f.id})">👁️ Ver Detalle</button>
                    <button class="btn btn-sm btn-success" onclick="descargarFacturaPDF(${f.id})">📥 Descargar PDF</button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarFactura(${f.id})">🗑️ Eliminar</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        lista.innerHTML = `<div class="mensaje error">Error al cargar facturas: ${error.message}</div>`;
    }
}

async function verDetalleFactura(id) {
    try {
        const result = await fetchAPI(`/facturas/${id}`);
        const f = result.data;

        const detallesHtml = f.detalles.map(d => `
            <tr>
                <td>${d.descripcion}</td>
                <td style="text-align: center;">${d.cantidad}</td>
                <td style="text-align: right;">${formatearMoneda(d.precioUnitario)}</td>
                <td style="text-align: right;">${formatearMoneda(d.subtotal)}</td>
            </tr>
        `).join('');

        document.getElementById('modal-body').innerHTML = `
            <h3>🧾 Factura ${f.numeroFactura}</h3>
            <div style="margin: 20px 0;">
                <p><strong>Propietario:</strong> ${f.propietarioNombre}</p>
                <p><strong>Mascota:</strong> ${f.mascotaNombre}</p>
                <p><strong>Fecha:</strong> ${f.fechaEmision}</p>
                <p><strong>Estado:</strong> ${f.estado}</p>
            </div>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Descripción</th>
                        <th>Cant.</th>
                        <th>Precio Unit.</th>
                        <th>Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    ${detallesHtml}
                </tbody>
                <tfoot style="background: #f3f4f6;">
                    <tr>
                        <td colspan="3" style="text-align: right;"><strong>Subtotal:</strong></td>
                        <td style="text-align: right;">${formatearMoneda(f.subtotal)}</td>
                    </tr>
                    <tr>
                        <td colspan="3" style="text-align: right;"><strong>IVA (${f.impuesto}%):</strong></td>
                        <td style="text-align: right;">${formatearMoneda(f.subtotal * f.impuesto / 100)}</td>
                    </tr>
                    <tr>
                        <td colspan="3" style="text-align: right;"><strong>TOTAL:</strong></td>
                        <td style="text-align: right; font-size: 1.2rem; color: #6366f1;"><strong>${formatearMoneda(f.total)}</strong></td>
                    </tr>
                </tfoot>
            </table>
            
            <div class="btn-group" style="margin-top: 20px;">
                <button class="btn btn-success" onclick="descargarFacturaPDF(${f.id})">📥 Descargar PDF</button>
                <button class="btn btn-secondary" onclick="cerrarModal()">Cerrar</button>
            </div>
        `;

        document.getElementById('modal').classList.add('active');
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

async function descargarFacturaPDF(id) {
    try {
        const result = await fetchAPI(`/facturas/${id}`);
        const f = result.data;

        // Usar jsPDF para generar el PDF
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        // Configurar fuente
        doc.setFont('helvetica');

        // Encabezado
        doc.setFillColor(99, 102, 241);
        doc.rect(0, 0, 210, 40, 'F');

        doc.setTextColor(255, 255, 255);
        doc.setFontSize(24);
        doc.text('VetApp', 20, 20);
        doc.setFontSize(12);
        doc.text('Sistema de Gestión Veterinaria', 20, 30);

        // Número de factura
        doc.setFontSize(16);
        doc.text(`Factura: ${f.numeroFactura}`, 130, 25);

        // Datos del cliente
        doc.setTextColor(0, 0, 0);
        doc.setFontSize(12);
        let y = 55;

        doc.setFontSize(14);
        doc.setFont('helvetica', 'bold');
        doc.text('Datos del Cliente', 20, y);
        doc.setFont('helvetica', 'normal');
        doc.setFontSize(11);
        y += 10;
        doc.text(`Propietario: ${f.propietarioNombre}`, 20, y);
        y += 7;
        doc.text(`Email: ${f.propietarioEmail}`, 20, y);
        y += 7;
        doc.text(`Mascota: ${f.mascotaNombre}`, 20, y);
        y += 7;
        doc.text(`Fecha: ${f.fechaEmision}`, 20, y);
        y += 7;
        doc.text(`Estado: ${f.estado}`, 20, y);

        // Línea separadora
        y += 10;
        doc.setDrawColor(200, 200, 200);
        doc.line(20, y, 190, y);

        // Detalle de servicios
        y += 10;
        doc.setFontSize(14);
        doc.setFont('helvetica', 'bold');
        doc.text('Detalle de Servicios', 20, y);

        // Encabezados de tabla
        y += 10;
        doc.setFillColor(240, 240, 240);
        doc.rect(20, y - 5, 170, 8, 'F');
        doc.setFontSize(10);
        doc.text('Descripción', 22, y);
        doc.text('Cant.', 110, y);
        doc.text('P. Unit.', 130, y);
        doc.text('Subtotal', 160, y);

        // Filas de detalle
        doc.setFont('helvetica', 'normal');
        f.detalles.forEach(d => {
            y += 8;
            doc.text(d.descripcion.substring(0, 40), 22, y);
            doc.text(d.cantidad.toString(), 115, y);
            doc.text(formatearMoneda(d.precioUnitario), 130, y);
            doc.text(formatearMoneda(d.subtotal), 160, y);
        });

        // Totales
        y += 15;
        doc.line(20, y, 190, y);
        y += 10;

        doc.setFont('helvetica', 'normal');
        doc.text('Subtotal:', 130, y);
        doc.text(formatearMoneda(f.subtotal), 160, y);

        y += 7;
        doc.text(`IVA (${f.impuesto}%):`, 130, y);
        doc.text(formatearMoneda(f.subtotal * f.impuesto / 100), 160, y);

        y += 10;
        doc.setFont('helvetica', 'bold');
        doc.setFontSize(14);
        doc.text('TOTAL:', 130, y);
        doc.setTextColor(99, 102, 241);
        doc.text(formatearMoneda(f.total), 160, y);

        // Pie de página
        doc.setTextColor(150, 150, 150);
        doc.setFontSize(9);
        doc.setFont('helvetica', 'normal');
        doc.text('Gracias por confiar en VetApp para el cuidado de su mascota.', 105, 280, { align: 'center' });

        // Descargar
        doc.save(`Factura_${f.numeroFactura}.pdf`);

        mostrarMensaje('PDF descargado exitosamente');
    } catch (error) {
        mostrarMensaje('Error al generar PDF: ' + error.message, 'error');
    }
}

async function eliminarFactura(id) {
    if (!confirm('¿Estás seguro de eliminar esta factura?')) return;

    try {
        await fetchAPI(`/facturas/${id}`, { method: 'DELETE' });
        mostrarMensaje('Factura eliminada');
        cargarFacturas();
    } catch (error) {
        mostrarMensaje(error.message, 'error');
    }
}

// ========================================
// MODAL
// ========================================

function cerrarModal() {
    document.getElementById('modal').classList.remove('active');
}

// Cerrar modal al hacer clic fuera
document.addEventListener('click', (e) => {
    const modal = document.getElementById('modal');
    if (e.target === modal) {
        cerrarModal();
    }
});

// Cerrar modal con Escape
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        cerrarModal();
    }
});