import UIKit
import SharedCode

/* class TableViewController: UIViewController,  UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    
    private var data:[String]=[String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.dataSource = self
        self.tableView.delegate = self
        
        data = ["Title 1", "Title 2"]
        
        self.registerTableViewCells()
    }
    
    func registerTableViewCells() {
        let trainCell = UINib(nibName: "TableViewCell", bundle: nil)
        self.tableView.register(trainCell, forCellReuseIdentifier: "TableViewCell")
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) ->
        UITableViewCell {
            
        if let cell = tableView.dequeueReusableCell(withIdentifier: "TableViewCell") as? TableViewCell{
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
}

class TableViewCell: UITableViewCell {
    
    @IBOutlet weak var departureLabel: UILabel!
    @IBOutlet weak var arrivalLabel: UILabel!
}*/
