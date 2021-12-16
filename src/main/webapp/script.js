const categoriesSelect = $('#categories');

categoriesSelect.selectpicker({
    noneSelectedText: 'Выберите категорию',
    width: 'fit',
    style: '',
    styleBase: 'form-control'
});

$(document).ready(function () {
    setNavigation();
    loadCategories();
    loadItems();
    setFormSubmittable();
});

const setFormSubmittable = () => {
    $("#form").submit(e => {
        e.preventDefault();

        const descriptionInput = $('#description');
        const selectedIds = categoriesSelect.val();

        if (descriptionInput.val() === undefined || selectedIds.length === 0) {
            alert('Введите название задания и выберите категорию');
        }

        $.ajax({
            type: 'POST',
            url: `${window.location.origin}/todo/items`,
            data: JSON.stringify({
                description: descriptionInput.val(),
                categoryIds: selectedIds.map(id => +id)
            }),
            dataType: 'json'
        }).done(item => {
            $('.todo-list').prepend(getNewItem(item));
            descriptionInput.val('');
            categoriesSelect.val('default').selectpicker("refresh");
            setItemsToggable();
        }).fail(err => {
            console.log(err);
        });
    });
}

const loadItems = () => {
    $.ajax({
        type: 'GET',
        url: `${window.location.origin}/todo/items`,
        dataType: 'json'
    }).done(items => {
        for (const item of items) {
            $('.todo-list').append(getNewItem(item));
        }
        setItemsToggable();
    }).fail(function (err) {
        console.log(err);
    });
}

const setItemsToggable = () => {
    $('.todo-list .todo-item input').click(function () {
        const id = $(this).attr('data-id');

        $.ajax({
            type: 'PUT',
            url: `${window.location.origin}/todo/items`,
            data: JSON.stringify({
                id
            }),
            dataType: 'json'
        }).done(item => {
            if (item.done) {
                $(this).parent().parent().parent().toggleClass('complete');
            } else {
                $(this).parent().parent().parent().toggleClass('complete');
            }
        }).fail(err => {
            console.log(err);
        });
    });
};

const setNavigation = () => {
    const list = $('.todo-list');

    $('.todo-nav .all-task').click(function () {
        list.removeClass('only-active');
        list.removeClass('only-complete');
        $('.todo-nav li.active').removeClass('active');
        $(this).addClass('active');
    });

    $('.todo-nav .active-task').click(function () {
        list.removeClass('only-complete');
        list.addClass('only-active');
        $('.todo-nav li.active').removeClass('active');
        $(this).addClass('active');
    });

    $('.todo-nav .completed-task').click(function () {
        list.removeClass('only-active');
        list.addClass('only-complete');
        $('.todo-nav li.active').removeClass('active');
        $(this).addClass('active');
    });
}

const logout = () => {
    $.ajax({
        type: 'GET',
        url: `${window.location.origin}/todo/logout`
    }).done(data => {
        window.location.href = `${window.location.origin}/todo/login`;
    }).fail(err => {
        console.log(err);
    });
}

const loadCategories = () => {
    $.ajax({
        type: 'GET',
        url: `${window.location.origin}/todo/categories`,
        dataType: 'json'
    }).done(categories => {
        console.log(categories)
        for (let category of categories) {
            categoriesSelect.append(`<option value=${category.id}>${category.name}</option>`)
        }
        categoriesSelect.selectpicker('refresh');
    }).fail(function (err) {
        console.log(err);
    });
}

const getNewItem = (item) => {
    console.log(item)
    return `
       <div class="todo-item ${item.done ? 'complete' : ''}">
           <div class="todo-item-header">
               <div class="checker">
                    <span>
                        <input type="checkbox" ${item.done ? 'checked' : ''} data-id=${item.id}>
                    </span>
                </div>
                <span class="todo-item-description">${item.description}</span>
                <span class="todo-item-author">автор: ${item.user.name}</span>
            </div>
            <div class="todo-item-footer">
                ${item.categories.map(category => category.name).join(', ')}
            </div>
       </div>
    `;
}