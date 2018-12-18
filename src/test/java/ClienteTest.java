import br.edu.unidavi.trabalhofinalapi.domain.model.Cliente;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClienteTest {

    @Test
    public void testSaveCliente() throws Exception {
        Cliente cliente = Cliente.of(1L, "059.887.698-95", "Davi Prada Hoffmann", "davi.hoffmann@unidavi.edu.br", Date.from(Instant.now()));

        assertNotNull(cliente);
        assertTrue(cliente.getId() != null);
    }

}
