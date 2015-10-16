package contatos.treinamento.com.br.contatos.model.util;

import java.io.File;

/**
 * Created by c1284521 on 16/10/2015.
 */
public final class ListHelper {


    public static void DeleteRecursive(File path)
    {
        if (path.isDirectory())
        {
            for (File child : path.listFiles())
            {
                DeleteRecursive(child);
            }
        }

        path.delete();
    }
}
