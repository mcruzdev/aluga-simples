# Próxima aula

* Falar sobre `@GeneratedValue`
* Falar sobre o `import.sql`
* Falar sobre RestAssured body Matchers
* Falar sobre profile %test, %prod, %dev
* Falar sobre o `@Table` e como podemos mudar o nome das tabelas no banco de dados

* Usar um banco de dados real como o postgresql com Docker
* Explicar DevServices (eles desligam mesmo tem tempo de desenvolvimento)
* Usar profile de dev para desligar o DevService e ligar em teste
* Explicar relacionamentos (um para um, um para muitos e muitos para muitos) **MOSTRAR a modelagem em desenho**
  * Vamos registrar agora as manutenções do carro (um carro tem muitas manutenções e uma manutenção tem apenas um carro)
  * Vamos registrar agora os acessórios do carro (um carro tem muitos acessórios e um acessório pode estar em muitos carros)
  * Explicar o relacionamento uniderecional e o bidirecional (**REVER**)

## Relacionamento unidirecional e bidirecional

### Quando Usar e Quando Evitar Relacionamentos Bidirecionais com JPA

Relacionamentos bidirecionais em JPA permitem a navegação entre entidades nos dois sentidos (por exemplo, `Vehicle` → `Maintenance` e `Maintenance` → `Vehicle`). Apesar de úteis, eles devem ser usados com cuidado.

---

## Quando usar relacionamentos bidirecionais

1. **Navegação frequente em ambos os lados**  
   Quando a lógica do negócio exige acessar ambas as entidades de forma recorrente.  
   *Exemplo:* um veículo com várias manutenções, e também uma manutenção que precisa saber de qual veículo ela veio.

2. **Exibição de dados em ambos os lados (DTOs compostos)**  
   Quando a visualização exige informações da entidade "pai" e da "filha".

3. **Sincronização em memória entre as entidades**  
   Útil quando é importante manter os dois lados atualizados no modelo de domínio, sem depender de queries adicionais.

---

## Quando evitar relacionamentos bidirecionais

1. **Navegação ocorre majoritariamente em um único sentido**  
   Criar o outro lado do relacionamento se torna redundante e adiciona complexidade desnecessária.

2. **Sincronização dos dois lados não é importante**  
   Você precisará manter os dois lados sincronizados manualmente (ex: `vehicle.getMaintenances().add(...)`), o que pode gerar bugs se esquecido.

3. **Simplicidade e performance**  
   Quanto mais relacionamentos bidirecionais, mais complexidade na serialização (ex: problemas com `StackOverflowError` ao usar JSON) e mais esforço para manutenção.

---

## Dica

Se optar por usar um relacionamento bidirecional, **sempre defina o lado "dono" com `@ManyToOne` e o lado inverso com `mappedBy` no `@OneToMany`**. Mantenha os métodos auxiliares sincronizados (ex: `addMaintenance(Maintenance m)` e `m.setVehicle(this)`).

```java
@Entity
public class Vehicle {
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maintenance> maintenances = new ArrayList<>();

    public void addMaintenance(Maintenance m) {
        maintenances.add(m);
        m.setVehicle(this);
    }
}

@Entity
public class Maintenance {
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
```

### Artigos super interessante sobre relacionamentos

* <https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/>
* <https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/>

### Bean validation

* Como instalar o bean validation
* Como anotar as classes com as anotações do bean validation
* Como utilizar o `@Valid`
* Como utilizar o `ConstraintValidator`:

```java
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

@ApplicationScoped
public class UsuarioService {

    private final Validator validator;

    public UsuarioService(Validator validator) {
        this.validator = validator;
    }

    public void validarUsuario(Usuario usuario) {
        Set<ConstraintViolation<Usuario>> violationSet = validator.validate(usuario);
        if (!violacoes.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder("Erro(s) de validação:\n");
            for (ConstraintViolation<Usuario> violation : violationSet) {
                mensagemErro.append(String.format("%s: %s%n", violation.getPropertyPath(), violation.getMessage()));
            }
            throw new IllegalArgumentException(mensagemErro.toString());
        }
    }
}
```

* Criando sua própria validação
* Utilizando `ExceptionMapper`
* Utilizando `ServerExceptionMapper`
