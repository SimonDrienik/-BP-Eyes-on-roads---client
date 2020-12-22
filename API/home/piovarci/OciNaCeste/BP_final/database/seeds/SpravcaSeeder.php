<?php

use App\Models\Spravca;
use Illuminate\Database\Seeder;

class SpravcaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $spravca = [

            [
                'nazov' => 'Nepriradeny',
            ],
            [
                'nazov' => 'Rychlostna cesta',
            ],
            [
                'nazov' => 'Cesta 1. triedy',
            ],
            [
                'nazov' => 'Cesta 2. triedy'
            ],
            [
                'nazov' => 'Cesta 3. triedy'
            ],

            [
                'nazov' => 'Miestna komunikacia'
            ]
        ];

        foreach ($spravca as $spravca) {
            Spravca::create(array(
                'nazov' => $spravca['nazov']
            ));
        }
    }
}
