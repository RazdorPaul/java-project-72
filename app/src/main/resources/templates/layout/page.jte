@import gg.jte.Content


@param String title
@param Content content
@param String flash

<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${title}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="/">Анализатор страниц</a>
                <button class="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarNav"
                        aria-controls="navbarNav"
                        aria-expanded="false"
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse"
                     id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link active"
                               aria-current="page"
                               href="/">Сайты</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <main class="container mt-4 flex-grow-1">
            @if (flash != null && !flash.isBlank())
                <div class="alert alert-info alert-dismissible fade show"
                     role="alert">
                    ${flash}
                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="alert"></button>
                </div>
            @endif
            ${content}
        </main>
        <footer class="bg-light border-top py-4 mt-auto">
            <div class="container text-center">
                <a href="https://hexlet.io"
                   class="text-decoration-none">Hexlet</a>
            </div>
        </footer>
    </body>
</html>
