async function cargarProductos() {

    try {

        const respuesta = await fetch("/api/productos");

        if (!respuesta.ok) {
            throw new Error("Error HTTP " + respuesta.status);
        }

        const productos = await respuesta.json();

        const tabla =
            document.getElementById("tablaProductos");

        const ventaSelect =
            document.getElementById("ventaProducto");

        const reponerSelect =
            document.getElementById("reponerProducto");

        tabla.innerHTML = "";

        ventaSelect.innerHTML =
            '<option value="">Selecciona un producto</option>';

        reponerSelect.innerHTML =
            '<option value="">Selecciona un producto</option>';

        productos.forEach(producto => {

            const stockBajo =
                producto.stock < 5;

            const fila =
                document.createElement("tr");

            fila.innerHTML = `
                <td>${producto.id}</td>
                <td>${producto.nombre}</td>
                <td>${Number(producto.precio).toFixed(2)} €</td>
                <td class="${stockBajo ? "stock-bajo" : ""}">
                    ${producto.stock}
                </td>
                <td>
                    ${stockBajo ? "Stock bajo" : "Disponible"}
                </td>
            `;

            tabla.appendChild(fila);

            const opcionVenta =
                document.createElement("option");

            opcionVenta.value =
                producto.id;

            opcionVenta.textContent =
                `${producto.nombre} - Stock: ${producto.stock}`;

            ventaSelect.appendChild(
                opcionVenta
            );
			
            const opcionReponer =
                document.createElement("option");

            opcionReponer.value =
                producto.id;

            opcionReponer.textContent =
                `${producto.nombre} - Stock: ${producto.stock}`;

            reponerSelect.appendChild(
                opcionReponer
            );
        });

    } catch (error) {

        console.error(error);

        mostrarMensaje(
            500,
            "No se pudieron cargar los productos"
        );
    }
}

async function realizarVenta() {

    const productoId =
        Number(
            document.getElementById(
                "ventaProducto"
            ).value
        );

    const cantidad =
        Number(
            document.getElementById(
                "ventaCantidad"
            ).value
        );

    if (!productoId || !cantidad) {

        mostrarMensaje(
            400,
            "Introduce producto y cantidad"
        );

        return;
    }

    try {

        const respuesta =
            await fetch(
                "/api/ventas",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({
                        productoId,
                        cantidad
                    })
                }
            );

        const datos =
            await respuesta.json();

        mostrarMensaje(
            respuesta.status,
            datos.mensaje || datos.error
        );

        if (respuesta.ok) {

            document.getElementById(
                "ventaCantidad"
            ).value = "";

            cargarProductos();
        }

    } catch (error) {

        mostrarMensaje(
            500,
            "Error de comunicación con el servidor"
        );
    }
}


async function reponerStock() {

    const productoId =
        Number(
            document.getElementById(
                "reponerProducto"
            ).value
        );

    const cantidad =
        Number(
            document.getElementById(
                "reponerCantidad"
            ).value
        );

    if (!productoId || !cantidad) {

        mostrarMensaje(
            400,
            "Introduce producto y cantidad"
        );

        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/productos/${productoId}/reponer`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({
                        cantidad
                    })
                }
            );

        const datos =
            await respuesta.json();

        mostrarMensaje(
            respuesta.status,
            datos.mensaje || datos.error
        );

        if (respuesta.ok) {

            document.getElementById(
                "reponerCantidad"
            ).value = "";

            cargarProductos();
        }

    } catch (error) {

        mostrarMensaje(
            500,
            "Error de comunicación con el servidor"
        );
    }
}


function mostrarMensaje(
    codigo,
    texto
) {

    const mensaje =
        document.getElementById(
            "mensaje"
        );

    mensaje.style.display =
        "block";

    mensaje.className =
        codigo >= 200 &&
        codigo < 300
            ? "mensaje correcto"
            : "mensaje error";

    mensaje.textContent =
        `HTTP ${codigo} - ${texto}`;
}

cargarProductos();