@import hexlet.code.dto.UrlPage
@import hexlet.code.dto.PageResultCheck
@import hexlet.code.utils.UrlUtils

@param UrlPage page
@param PageResultCheck pageCheck

@template.layout.page(
flash = page.getFlash(),
title = "Сайт: " + page.getUrl().getName(),
content = @`
    <h1 class="display-6">Сайт: ${page.getUrl().getName()}</h1>

    <table data-test="url" class="table table-bordered">
        <tbody>
        <tr>
            <td>ID</td>
            <td>${page.getUrl().getId()}</td>
        </tr>
        <tr>
            <td>Имя</td>
            <td>${page.getUrl().getName()}</td>
        </tr>
        <tr>
            <td>Дата создания</td>
            <td>${String.valueOf(page.getUrl().getCreatedAt().toLocalDateTime().toLocalDate())}</td>
        </tr>
        </tbody>
    </table>

    <h2 class="mt-5">Проверки</h2>
    <form action="/urls/${page.getUrl().getId()}/checks" method="post">
        <button type="submit" class="btn btn-primary mb-3">Запустить проверку</button>
    </form>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Код ответа</th>
            <th>h1</th>
            <th>title</th>
            <th>description</th>
            <th>Дата создания</th>
        </tr>
        </thead>
        <tbody>
            @for (var check : pageCheck.getChecks())
                <tr>
                    <td>${check.getId()}</td>
                    <td>${check.getStatusCode()}</td>
                    <td>${UrlUtils.truncate(check.getH1(), 200)}</td>
                    <td>${UrlUtils.truncate(check.getTitle(), 200)}</td>
                    <td>${UrlUtils.truncate(check.getDescription(), 200)}</td>
                    <td>${UrlUtils.formatDate(check.getCreatedAt())}</td>
                </tr>
            @endfor
        </tbody>
    </table>
`
)