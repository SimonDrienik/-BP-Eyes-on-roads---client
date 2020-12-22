<?php

use App\Models\FotkaProblemu;
use Illuminate\Database\Seeder;

class FotkaProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $fotky = [
            [
                'cesta_k_suboru' => 'C:\file1\file2\fotka1.png',
                'problem_id' => 1
            ],
            [
                'cesta_k_suboru' => 'C:\file1\file2\fotka2.png',
                'problem_id' => 1
            ],
            [
                'cesta_k_suboru' => 'C:\file1\file2\fotka3.png',
                'problem_id' => 1
            ]
        ];

        foreach ($fotky as $fotka_problemu) {
            FotkaProblemu::create(array(
                'cesta_k_suboru' => $fotka_problemu['cesta_k_suboru'],
                'problem_id' => $fotka_problemu['problem_id']
            ));
        }
    }
}
