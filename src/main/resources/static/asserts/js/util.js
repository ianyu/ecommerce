function toggleSelectAll(source, checkboxClass) {
  const checkboxes = document.querySelectorAll(checkboxClass);
  checkboxes.forEach(checkbox => {
    checkbox.checked = source.checked;
  });
}

function closeModal(modalId) {
  document.getElementById(modalId).style.display = 'none';
}