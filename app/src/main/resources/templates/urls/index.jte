@import hexlet.code.dto.UrlsPage
@import hexlet.code.utils.UrlUtils
@import java.time.format.DateTimeFormatter

@param UrlsPage page

@template.layout.page(
flash = page.getFlash(),
title = page.getTitle(),
content = @`
    <h1 class="display-5">Сайты</h1>

    <table data-test="urls" class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Последняя проверка</th>
            <th>Код ответа</th>
        </tr>
        </thead>
        <tbody>
        @for (var url : page.getUrls())
            <tr>
                <td>${url.getId()}</td>
                <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                <td>
                    @if (!url.getChecks().isEmpty())
                        ${UrlUtils.formatDate(url.getChecks().getFirst().getCreatedAt())}
                    @endif
                </td>
                <td>
                    @if (!url.getChecks().isEmpty())
                        ${url.getChecks().getFirst().getStatusCode()}
                    @endif
                </td>
            </tr>
        @endfor
        </tbody>
    </table>
`
)