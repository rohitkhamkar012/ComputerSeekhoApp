using ComputerSeekho.Net.Models;

namespace ComputerSeekho.Net.IServices;

public interface IPaymentService
{
    Task<Receipt?> ProcessPaymentAsync(Payment payment);
    Task<List<Payment>> GetAllPaymentsAsync();
    Task<List<Payment>> GetPaymentsByStudentIdAsync(int studentId);
}
